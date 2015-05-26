package com.github.vitaliyyarovuy;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

/**
 * Created by vyarovuy on 25.05.2015.
 */
public class RunTestInBrowserActionClass extends AnAction {
    public void actionPerformed(AnActionEvent event) {
        // TODO: insert action logic here
        Project project = event.getData(PlatformDataKeys.PROJECT);
        PsiFile file = event.getData(PlatformDataKeys.PSI_FILE);
        assert file != null;
        String filePath = file.getVirtualFile().getPath();

        ChutzpahRun runTask = new ChutzpahRun();
        runTask.runTestInBrowser(project, filePath);
    }

}
