package com.github.ybroeker.pmdidea.actions.scan;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.github.ybroeker.pmdidea.pmd.*;
import com.github.ybroeker.pmdidea.toolwindow.PmdToolPanel;
import com.github.ybroeker.pmdidea.toolwindow.PmdToolWindowFactory;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;


public abstract class AbstractScanAction extends AnAction {

    protected abstract List<File> getFiles(@NotNull final Project project);

    private List<ScannableFile> getScannableFiles(@NotNull final Project project) {
        List<ScannableFile> scannableFiles = new ArrayList<>();
        for (final File file : getFiles(project)) {
            scannableFiles.add(new LocalFile(file));
        }
        return scannableFiles;
    }

    @Override
    public void update(@NotNull final AnActionEvent event) {
        final Project project = event.getData(CommonDataKeys.PROJECT);
        if (project == null) {
            return;
        }

        final boolean hasValidRules = project.getService(RulesService.class).hasValidRuleSet();

        final Presentation presentation = event.getPresentation();
        if (hasValidRules != presentation.isEnabled()) {
            presentation.setEnabled(hasValidRules);
        }
    }

    private boolean isEnabled(final Project project) {
        if (project == null) {
            return false;
        }

        if (!project.getService(RulesService.class).hasValidRuleSet()) {
            return false;
        }

        return true;
    }

    @Override
    public final void actionPerformed(final AnActionEvent event) {
        final Project project = event.getData(CommonDataKeys.PROJECT);
        if (!isEnabled(project)) {
            return;
        }

        final PmdRunListener pmdRunListener = createRunListener(project);

        final List<ScannableFile> files = getScannableFiles(project);
        final PmdConfigurationFactory configurationFactory = project.getService(PmdConfigurationFactory.class);
        final PmdConfiguration configuration = configurationFactory.getPmdConfiguration(files, pmdRunListener);


        final PmdAdapter pmdAdapter = project.getService(PmdAdapterDelegate.class);
        //TODO: check if real files are needed
        //TODO: ReadAction needed?
        ApplicationManager.getApplication().saveAll();
        ApplicationManager.getApplication().executeOnPooledThread(() -> pmdAdapter.runPmd(configuration));
    }

    private PmdRunListener createRunListener(final Project project) {
        final ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(PmdToolWindowFactory.TOOL_WINDOW_ID);

        final PmdToolPanel toolPanel = (PmdToolPanel) toolWindow.getContentManager().getContent(0).getComponent();

        return new PmdRunListenerAdapter(toolPanel);
    }


}
