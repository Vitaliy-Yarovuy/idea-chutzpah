package com.github.vitaliyyarovuy;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;

/**
 * Created by vyarovuy on 25.05.2015.
 */
public class RunAllTestFromPathActionClass extends AnAction {
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        PsiDirectory dir = (PsiDirectory)event.getData(PlatformDataKeys.PSI_ELEMENT);
        assert dir != null;
        String filePath = dir.getVirtualFile().getPath();

        ChutzpahRun runTask = new ChutzpahRun();
        runTask.runAllTestsFromPath(project, filePath);
    }

}
