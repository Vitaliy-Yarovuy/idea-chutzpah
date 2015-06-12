package com.github.vitaliyyarovuy;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * Created by vyarovuy on 25.05.2015.
 */
public class RunAllTestFromPathActionClass extends AnAction {
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        PsiElement element = event.getData(PlatformDataKeys.PSI_ELEMENT);
        assert  element != null;

        String filePath = null;
        if(element instanceof PsiDirectory){
            filePath = ((PsiDirectory)element).getVirtualFile().getPath();
        } else if(element instanceof PsiFile){
            filePath = ((PsiFile)element).getVirtualFile().getPath();
        }

        if(filePath != null){
            ChutzpahRun runTask = new ChutzpahRun();
            runTask.runAllTestsFromPath(project, filePath);
        }
    }

}
