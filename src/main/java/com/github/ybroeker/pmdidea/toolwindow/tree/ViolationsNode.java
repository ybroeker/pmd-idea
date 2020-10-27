package com.github.ybroeker.pmdidea.toolwindow.tree;

import java.util.*;

import com.github.ybroeker.pmdidea.Level;
import net.sourceforge.pmd.RulePriority;

public interface ViolationsNode {

    int getViolationsCount();

    default boolean hasAnyLevel(final Set<Level> levels) {
        for (final RulePriority rulePriority : getRulePriorities()) {
            for (final Level level : levels) {
                if (level.matches(rulePriority)) {
                    return true;
                }
            }
        }
        return false;
    }

    Set<RulePriority> getRulePriorities();

    default List<? extends ViolationsNode> getChildren() {
        return Collections.emptyList();
    }

    boolean isLeaf();
}
