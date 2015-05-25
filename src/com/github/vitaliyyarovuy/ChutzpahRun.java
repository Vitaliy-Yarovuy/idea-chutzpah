package com.github.vitaliyyarovuy;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionHelper;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.Ref;
import com.intellij.util.concurrency.Semaphore;
import org.apache.commons.codec.Charsets;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by vyarovuy on 25.05.2015.
 */
public class ChutzpahRun {

    public boolean executeTask(final Project project, final String filePath) {

        final Semaphore targetDone = new Semaphore();
        final List<Exception> exceptions = new ArrayList<Exception>();
        final Ref<Boolean> result = new Ref<Boolean>(true);

        ApplicationManager.getApplication().invokeAndWait(new Runnable() {
            @Override
            public void run() {

                targetDone.down();
                new Task.Backgroundable(project, "run chutzpah.console /openInBrowser", true) {

                    public boolean shouldStartInBackground() {
                        return true;
                    }

                    public void run(@NotNull final ProgressIndicator indicator) {
                        try {
                            String chuzpah = "chutzpah.console.exe";
                            ProcessOutput output = executeAndGetOut(new String[]{chuzpah, filePath, "/openInBrowser"});
                            if (output.getExitCode() != 0) {
                                ExecutionHelper.showOutput(project, output, "run chutzpah.console /openInBrowser", null, true);
                                result.set(false);
                                targetDone.up();
                                return;
                            }

                            targetDone.up();
                        }
                        catch (final ExecutionException e) {
                            exceptions.add(e);
                            result.set(false);
                            targetDone.up();
                        }
                    }
                }.queue();
            }
        }, ModalityState.NON_MODAL);
        targetDone.waitFor();
        if (!exceptions.isEmpty()) {
            ExecutionHelper.showErrors(project, exceptions, "Cannot Init Platform", null);
        }
        return result.get();

    }



    private ProcessOutput executeAndGetOut(String[] command) throws ExecutionException {
        final GeneralCommandLine commandLine = new GeneralCommandLine(command);

        Process process = commandLine.createProcess();
        commandLine.setPassParentEnvironment(true);
        OSProcessHandler processHandler = new ColoredProcessHandler(process, commandLine.getCommandLineString(), Charsets.UTF_8);
        final ProcessOutput output = new ProcessOutput();
        processHandler.addProcessListener(new ProcessAdapter() {
            @Override
            public void onTextAvailable(ProcessEvent event, Key outputType) {
                if (outputType == ProcessOutputTypes.STDERR) {
                    output.appendStderr(event.getText());
                }
                else if (outputType != ProcessOutputTypes.SYSTEM) {
                    output.appendStdout(event.getText());
                }
            }
        });
        processHandler.startNotify();
        if (processHandler.waitFor(TimeUnit.SECONDS.toMillis(12000))) {
            output.setExitCode(process.exitValue());
        }
        else {
            processHandler.destroyProcess();
            output.setTimeout();
        }
        return output;
    }


}
