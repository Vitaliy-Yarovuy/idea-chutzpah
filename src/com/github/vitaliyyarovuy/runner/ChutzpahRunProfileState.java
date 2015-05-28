package com.github.vitaliyyarovuy.runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by vyarovuy on 28.05.2015.
 */
public class ChutzpahRunProfileState extends CommandLineState {
    public ChutzpahRunProfileState(@NotNull ExecutionEnvironment env) {
        super(env);
    }
        @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        return null;
    }
}
