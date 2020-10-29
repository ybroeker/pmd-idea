package com.github.ybroeker.pmdidea.toolwindow.tree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import java.util.HashMap;
import java.util.Map;

import com.github.ybroeker.pmdidea.pmd.PmdRulePriority;
import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import net.sourceforge.pmd.RulePriority;
import net.sourceforge.pmd.RuleViolation;
import org.jetbrains.annotations.NotNull;


public class ResultTreeRenderer extends ColoredTreeCellRenderer {

    private static final long serialVersionUID = 1L;

    private static final Map<PmdRulePriority, Icon> ICONS = createIcons();

    private final Project project;

    public ResultTreeRenderer(final Project project) {
        super();
        this.project = project;
    }

    @Override
    public void customizeCellRenderer(@NotNull final JTree tree, final Object value, final boolean selected, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
        if (!(value instanceof DefaultMutableTreeNode)) {
            return;
        }
        final Object userObject = ((DefaultMutableTreeNode) value).getUserObject();


        if (userObject instanceof RootNode) {
            final RootNode object = (RootNode) userObject;
            final String name = project.getName();
            //this.append(String.format("%s (%d violations)", object.getViolationsCount()));

            this.setIcon(AllIcons.Nodes.Project);
            this.append(name, SimpleTextAttributes.REGULAR_ATTRIBUTES);
            this.append(" (" + object.getViolationsCount() + " Violations)", SimpleTextAttributes.GRAY_ATTRIBUTES);

            return;
        }

        if (userObject instanceof FileNode) {
            final FileNode fileNode = (FileNode) userObject;
            this.setIcon(AllIcons.FileTypes.Java);

            this.append(fileNode.getFileName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
            this.append(" (" + fileNode.getViolationsCount() + " Violations)", SimpleTextAttributes.GRAY_ATTRIBUTES);

            this.setToolTipText(fileNode.getFilePath());

            return;
        }

        if (userObject instanceof ViolationNode) {
            final ViolationNode violationNode = (ViolationNode) userObject;
            final PmdRuleViolation violation = violationNode.getViolation();

            this.setIcon(ICONS.get(violation.getPmdRule().getPmdRulePriority()));

            this.append(violation.getMessage(),
                    SimpleTextAttributes.REGULAR_ATTRIBUTES);
            this.append(" (" + violation.getPosition().getBeginLine() + ":" + violation.getPosition().getBeginColumn() + ")",
                    SimpleTextAttributes.REGULAR_ATTRIBUTES);
            this.append(" [" + violation.getPmdRule().getName() + "]",
                    SimpleTextAttributes.GRAYED_ATTRIBUTES);

            this.setToolTipText(violation.getPmdRule().getDescription());

            return;
        }
    }

    private static Map<PmdRulePriority, Icon> createIcons() {
        final Map<PmdRulePriority, Icon> icons = new HashMap<>();
        icons.put(PmdRulePriority.HIGH, AllIcons.General.Error);
        icons.put(PmdRulePriority.MEDIUM_HIGH, AllIcons.General.Warning);
        icons.put(PmdRulePriority.MEDIUM, AllIcons.General.Warning);
        icons.put(PmdRulePriority.MEDIUM_LOW, AllIcons.General.Information);
        icons.put(PmdRulePriority.LOW, AllIcons.General.Information);
        return icons;
    }

}
