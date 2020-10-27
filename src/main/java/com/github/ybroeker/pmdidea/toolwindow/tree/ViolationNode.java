package com.github.ybroeker.pmdidea.toolwindow.tree;

import java.util.*;

import net.sourceforge.pmd.RulePriority;
import net.sourceforge.pmd.RuleViolation;


public class ViolationNode implements ViolationsNode {

    private final RuleViolation violation;

    public ViolationNode(final RuleViolation violation) {
        this.violation = violation;
    }

    public RuleViolation getViolation() {
        return violation;
    }

    @Override
    public int getViolationsCount() {
        return 1;
    }

    @Override
    public Set<RulePriority> getRulePriorities() {
        return Collections.singleton(violation.getRule().getPriority());
    }

    @Override
    public boolean isLeaf() {
        return true;
    }
}
