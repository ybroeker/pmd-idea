package com.github.ybroeker.pmdidea.toolwindow.tree;

import java.util.*;

import com.github.ybroeker.pmdidea.Level;
import com.github.ybroeker.pmdidea.pmd.PmdRulePriority;
import net.sourceforge.pmd.RulePriority;

public interface ViolationsNode {

    int getViolationsCount();

    default boolean hasAnyLevel(final Set<Level> levels) {
        for (final PmdRulePriority rulePriority : getRulePriorities()) {
            for (final Level level : levels) {
                if (level.matches(rulePriority)) {
                    return true;
                }
            }
        }
        return false;
    }

    Set<PmdRulePriority> getRulePriorities();

    default List<? extends ViolationsNode> getChildren() {
        return Collections.emptyList();
    }

    boolean isLeaf();
}
