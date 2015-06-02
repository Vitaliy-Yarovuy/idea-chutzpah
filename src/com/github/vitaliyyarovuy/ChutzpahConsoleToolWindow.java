package com.github.vitaliyyarovuy;

import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import icons.ChutzpahIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class ChutzpahConsoleToolWindow {

    private static final String TOOL_WINDOW_ID = "Chutzpah Console";

    private final ConsoleView myConsoleView;
    private final JPanel myPanel;
    private final ToolWindow myToolWindow;
    private final ContentManager myContentManager;
    private static ChutzpahConsoleToolWindow instance;

    private ChutzpahConsoleToolWindow(@NotNull Project project) {

        myToolWindow = ToolWindowManager.getInstance(project).registerToolWindow(TOOL_WINDOW_ID,
                true,
                ToolWindowAnchor.BOTTOM,
                project,
                true);
        myToolWindow.setToHideOnEmptyContent(true);
        myToolWindow.setIcon(ChutzpahIcons.SmallIcon);
        myToolWindow.setTitle("hello everyone");
        myToolWindow.setAutoHide(true);
        myToolWindow.setSplitMode(true, null);
        myContentManager = myToolWindow.getContentManager();


        TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
        consoleBuilder.setViewer(true);
        myConsoleView = consoleBuilder.getConsole();

        myPanel = createPanel(myConsoleView);
    }

    public static ChutzpahConsoleToolWindow getInstance(@NotNull Project project){
        if(instance == null){
            instance = new ChutzpahConsoleToolWindow(project);
            instance.setAvailable(true);
        }
        return instance;
    }

    private static JPanel createPanel(@NotNull ConsoleView consoleView){
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        JComponent consoleComponent = consoleView.getComponent();
        panel.add(consoleComponent, BorderLayout.CENTER);
        ActionToolbar consoleActionToolbar = createActionToolbar(consoleView);
        consoleActionToolbar.setTargetComponent(consoleComponent);
        panel.add(consoleActionToolbar.getComponent(), BorderLayout.WEST);
        return panel;
    }

    @NotNull
    private static ActionToolbar createActionToolbar(@NotNull ConsoleView consoleView) {
        DefaultActionGroup group = new DefaultActionGroup();
        AnAction[] actions = consoleView.createConsoleActions();
        for (AnAction action : actions) {
            group.add(action);
        }
        return ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, group, false);
    }


    public void setAvailable(boolean available) {
        if (available) {
            if (myContentManager.getContentCount() == 0) {
                Content content = myContentManager.getFactory().createContent(myPanel, null, true);
                content.setCloseable(true);
                myContentManager.addContent(content);
            }
        }
        else {
            myContentManager.removeAllContents(true);
        }
        myToolWindow.setAvailable(available, null);
    }

    public void show() {
        if (myToolWindow.isAvailable()) {
            myToolWindow.show(null);
        }
    }

    public void attachToProcess(@NotNull OSProcessHandler processHandler) {
        processHandler.addProcessListener(new ProcessAdapter() {
            @Override
            public void onTextAvailable(ProcessEvent event, Key outputType) {
                ConsoleViewContentType contentType = ConsoleViewContentType.getConsoleViewType(outputType);
                myConsoleView.print(event.getText(), contentType);
            }
        });
    }
}
