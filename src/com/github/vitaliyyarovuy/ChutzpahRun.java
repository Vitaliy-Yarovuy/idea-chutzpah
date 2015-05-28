package com.github.vitaliyyarovuy;

import com.github.vitaliyyarovuy.runner.ChutzpahConfigurationType;
import com.github.vitaliyyarovuy.runner.ChutzpahRunConfiguration;
import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.process.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.testframework.TestConsoleProperties;
import com.intellij.execution.testframework.sm.runner.SMTRunnerConsoleProperties;
import com.intellij.execution.testframework.sm.runner.ui.SMTRunnerConsoleView;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.util.concurrency.Semaphore;
import org.apache.commons.codec.Charsets;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by vyarovuy on 25.05.2015.
 */
public class ChutzpahRun {

    final static String CHUZPAH = "chutzpah.console.exe";

    private void executeTaskInBackground(final Project project, final String[] commands, final
    boolean isLog) {

        final List<Exception> exceptions = new ArrayList<Exception>();
        final Ref<Boolean> result = new Ref<Boolean>(true);

        ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
            @Override
            public void run() {

                try {

                    String tabDisplayName = "chutzpah.console run";
                    ExecutionHelper.ErrorViewPanel errorTreeView = new ExecutionHelper.ErrorViewPanel(project);

                    ChutzpahConfigurationType type = new ChutzpahConfigurationType();

                    ChutzpahRunConfiguration runConfiguration = new ChutzpahRunConfiguration(project, type
                            .getConfigurationFactories()[0] , "some label");
                    ///////////////////////////

                    TestConsoleProperties consoleProperties = new SMTRunnerConsoleProperties(runConfiguration,
                            "CHUTZPAH", new DefaultRunExecutor());
                    ///////////////////
                    SMTRunnerConsoleView  consoleView = new SMTRunnerConsoleView(consoleProperties, new ExecutionEnvironment());

                    final GeneralCommandLine commandLine = new GeneralCommandLine(commands);

                    Process process = commandLine.createProcess();
                    commandLine.setPassParentEnvironment(true);
                    OSProcessHandler processHandler = new ColoredProcessHandler(process, commandLine.getCommandLineString(), Charsets.UTF_8);
                    consoleView.attachToProcess(processHandler);
                    //final ProcessOutput output = new ProcessOutput();
//                    processHandler.addProcessListener(new ProcessAdapter() {
//                        @Override
//                        public void onTextAvailable(ProcessEvent event, Key outputType) {
//                            if (outputType == ProcessOutputTypes.STDERR) {
//                                output.appendStderr(event.getText());
//                                ExecutionHelper.showOutput(project, output, "run chutzpah.console", null, true);
//                            }
//                            else if (outputType != ProcessOutputTypes.SYSTEM) {
//                                output.appendStdout(event.getText());
//                            }
//                        }
//                    });
                    processHandler.startNotify();
                    if (processHandler.waitFor(TimeUnit.SECONDS.toMillis(12000))) {
                        //output.setExitCode(process.exitValue());
                    }
                    else {
                        processHandler.destroyProcess();
                        //output.setTimeout();
                    }
                } catch (final ExecutionException e) {
                    //exceptions.add(e);
                    //result.set(false);
                }
            }

        });
    }




    public void runTestInBrowser(final Project project, final String filePath){
        executeTaskInBackground(project, new String[]{ ChutzpahRun.CHUZPAH, filePath, "/openInBrowser"}, false);
    }

    public void runAllTestsFromPath(final Project project, final String filePath){
        executeTaskInBackground(project, new String[]{ ChutzpahRun.CHUZPAH, filePath}, true);
    }

}
