package com.github.vitaliyyarovuy;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.process.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import org.apache.commons.codec.Charsets;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vyarovuy on 25.05.2015.
 */
public class ChutzpahRun {

    final static String CHUZPAH = "chutzpah.console.exe";

    private void executeTaskInBackground(final Project project, final String[] commands, final
    boolean isLog) {

        final List<Exception> exceptions = new ArrayList<Exception>();
        final Ref<Boolean> result = new Ref<Boolean>(true);

        final ChutzpahConsoleToolWindow consoleToolWindow = ChutzpahConsoleToolWindow.getInstance(project);
        if(isLog) {
            consoleToolWindow.show();
        }
        ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final GeneralCommandLine commandLine = new GeneralCommandLine(commands);

                    Process process = commandLine.createProcess();
                    commandLine.setPassParentEnvironment(true);
                    OSProcessHandler processHandler = new ColoredProcessHandler(process, commandLine.getCommandLineString(), Charsets.UTF_8);
                    if(isLog){
                        consoleToolWindow.attachToProcess(processHandler);
                        processHandler.startNotify();
                    }
                    processHandler.waitFor();
                } catch (final ExecutionException e) {
                    List<ExecutionException> errors = new ArrayList<ExecutionException>();
                    errors.add(e);
                    ExecutionHelper.showErrors(project, errors, "chutzpah error", null );
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
