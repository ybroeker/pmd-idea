package com.github.ybroeker.pmdidea.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.*;
import org.jetbrains.annotations.NotNull;


public class PmdToolWindowFactory implements ToolWindowFactory {

    public static final String TOOL_WINDOW_ID = "PMD-IDEA";

    @Override
    public void createToolWindowContent(@NotNull final Project project, @NotNull final ToolWindow toolWindow) {
        final ContentFactory contentFactory = toolWindow.getContentManager().getFactory();
        final Content content = contentFactory.createContent(new PmdToolPanel(project), null, false);
        toolWindow.getContentManager().addContent(content);
    }

}


