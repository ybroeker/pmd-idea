package com.github.ybroeker.pmdidea.toolwindow.tree;

import java.util.List;
import java.util.function.Predicate;

import javax.swing.event.TreeModelEvent;
import javax.swing.tree.*;

import com.intellij.util.ui.tree.AbstractTreeModel;

public class FilterableTreeModel extends AbstractTreeModel {

    private final DefaultMutableTreeNode root;

    private Predicate<ViolationsNode> filter;

    public FilterableTreeModel(final List<FileNode> fileViolations, final Predicate<ViolationsNode> filter) {
        this.root = new DefaultMutableTreeNode(new RootNode(fileViolations));
        this.filter = filter;
    }

    public void setFilter(final Predicate<ViolationsNode> filter) {
        //TODO: Test
        this.filter = filter;
        if (!listeners.isEmpty()) {
            final TreeNode[] treePath = {root};
            listeners.treeStructureChanged(new TreeModelEvent(this, treePath, null, null));
        }
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(final Object parent, final int index) {
        final ViolationsNode object = (ViolationsNode) ((DefaultMutableTreeNode) parent).getUserObject();

        int curIndex = -1;
        for (final ViolationsNode child : object.getChildren()) {
            if (filter.test(child)) {
                curIndex++;
            }
            if (curIndex == index) {
                return new DefaultMutableTreeNode(child);
            }
        }

        return null;
    }

    @Override
    public int getChildCount(final Object parent) {
        final ViolationsNode object = (ViolationsNode) ((DefaultMutableTreeNode) parent).getUserObject();

        int count = 0;
        for (final ViolationsNode child : object.getChildren()) {
            if (filter.test(child)) {
                count++;
            }
        }

        return count;
    }

    @Override
    public boolean isLeaf(final Object node) {
        final ViolationsNode object = (ViolationsNode) ((DefaultMutableTreeNode) node).getUserObject();
        return object.isLeaf();
    }

    @Override
    public int getIndexOfChild(final Object parent, final Object child) {
        final ViolationsNode parentObject = (ViolationsNode) ((DefaultMutableTreeNode) parent).getUserObject();
        final ViolationsNode childObject = (ViolationsNode) ((DefaultMutableTreeNode) child).getUserObject();

        int curIndex = -1;
        for (final ViolationsNode cur : parentObject.getChildren()) {
            if (filter.test(cur)) {
                curIndex++;
            }
            if (cur.equals(childObject)) {
                return curIndex;
            }
        }

        return -1;
    }
}
