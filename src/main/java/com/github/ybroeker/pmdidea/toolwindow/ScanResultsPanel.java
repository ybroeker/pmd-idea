package com.github.ybroeker.pmdidea.toolwindow;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.github.ybroeker.pmdidea.LevelFilterModel;
import com.github.ybroeker.pmdidea.toolwindow.tree.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;
import net.sourceforge.pmd.RuleViolation;

public class ScanResultsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final Project project;

    private final LevelFilterModel levelFilterModel;

    private final ScanProgressPanel scanProgressPanel = new ScanProgressPanel();

    private final JTree tree;

    private FilterableTreeModel currentTreeModel;

    public ScanResultsPanel(final Project project) {
        super(new BorderLayout());
        this.project = project;
        this.levelFilterModel = project.getService(LevelFilterModel.class);
        levelFilterModel.addPropertyChangeListener(evt -> {
            if (currentTreeModel != null) {
                currentTreeModel.setFilter(violationsNode -> violationsNode.hasAnyLevel(levelFilterModel.getSelected()));
            }
        });

        add(scanProgressPanel, BorderLayout.NORTH);
        tree = new Tree();
        tree.setCellRenderer(new ResultTreeRenderer(project));

        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(final TreeSelectionEvent treeSelectionEvent) {
                final DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treeSelectionEvent.getPath().getLastPathComponent();
                onSelection(treeNode);
            }
        });

        tree.addMouseListener(new MouseAdapter() {
            private DefaultMutableTreeNode getSelectedNode() {
                if (tree.getSelectionPath() == null) {
                    return null;
                }
                return (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            }

            @Override
            public void mousePressed(final MouseEvent event) {
                final DefaultMutableTreeNode treeNode = getSelectedNode();
                if (treeNode == null) {
                    return;
                }
                if (event.getClickCount() == 2) {
                    onDoubleClick(treeNode);
                }
            }

        });

        add(new JBScrollPane(tree), BorderLayout.CENTER);
    }

    private void onSelection(final DefaultMutableTreeNode treeNode) {
    }

    private void onDoubleClick(final DefaultMutableTreeNode treeNode) {
        showInEditor(treeNode);
    }

    private void showInEditor(final DefaultMutableTreeNode treeNode) {
        if (treeNode != null) {
            final Object obj = treeNode.getUserObject();
            if (obj instanceof ViolationNode) {
                showInEditor((ViolationNode) obj);
            }
        }
    }

    private void showInEditor(final ViolationNode violation) {
        final VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(violation.getViolation().getFilename());
        if (virtualFile == null) {
            return;
        }

        final FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        fileEditorManager.navigateToTextEditor(new OpenFileDescriptor(
                        project,
                        virtualFile,
                        Math.max(violation.getViolation().getBeginLine() - 1, 0),
                        Math.max(violation.getViolation().getBeginColumn() - 1, 0)),
                true);
    }


    public ScanProgressPanel getScanProgressPanel() {
        return scanProgressPanel;
    }

    public void displayViolations(final List<RuleViolation> violations) {
        final Map<String, List<RuleViolation>> collect = violations.stream().collect(Collectors.groupingBy(RuleViolation::getFilename));

        final List<FileNode> fileNodes = new ArrayList<>();
        for (final Map.Entry<String, List<RuleViolation>> stringListEntry : collect.entrySet()) {
            final List<ViolationNode> collect1 = stringListEntry.getValue().stream().map(ViolationNode::new).collect(Collectors.toList());
            final FileNode fileNode = new FileNode(stringListEntry.getKey(), collect1);
            fileNodes.add(fileNode);
        }

        fileNodes.sort(Comparator.comparing(FileNode::getFileName));

        this.currentTreeModel = new FilterableTreeModel(fileNodes, violationsNode -> violationsNode.hasAnyLevel(levelFilterModel.getSelected()));

        tree.setModel(currentTreeModel);
    }
}
