package com.github.ybroeker.pmdidea.toolwindow;

import java.awt.*;

import javax.swing.*;
import java.util.List;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.util.ui.JBUI;
import net.sourceforge.pmd.RuleViolation;

public class PmdToolPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final ScanResultsPanel scanResultsPanel;

    public PmdToolPanel(final Project project) {
        super(new BorderLayout());
        scanResultsPanel = new ScanResultsPanel(project);

        final ActionGroup mainActionGroup = (ActionGroup) ActionManager.getInstance().getAction("PmdPluginActions");
        final ActionToolbar mainToolbar = ActionManager.getInstance().createActionToolbar(
                PmdToolWindowFactory.TOOL_WINDOW_ID, mainActionGroup, false);

        final ActionGroup resultsActionGroup = (ActionGroup) ActionManager.getInstance().getAction("PmdResultActions");
        final ActionToolbar resultsToolbar = ActionManager.getInstance().createActionToolbar(
                PmdToolWindowFactory.TOOL_WINDOW_ID, resultsActionGroup, false);


        setBorder(JBUI.Borders.empty(1));

        final Box box = Box.createHorizontalBox();
        box.add(mainToolbar.getComponent());
        box.add(resultsToolbar.getComponent());
        add(box, BorderLayout.WEST);
        add(scanResultsPanel, BorderLayout.CENTER);
    }

    public ScanProgressPanel getScanProgress() {
        return scanResultsPanel.getScanProgressPanel();
    }

    public void displayViolations(final List<PmdRuleViolation> violations) {
        scanResultsPanel.displayViolations(violations);
    }
}
