package com.github.ybroeker.pmdidea.toolwindow.tree;

import java.util.*;

import com.github.ybroeker.pmdidea.pmd.PmdRulePriority;
import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import net.sourceforge.pmd.RulePriority;
import net.sourceforge.pmd.RuleViolation;


public class ViolationNode implements ViolationsNode {

    private final PmdRuleViolation violation;

    public ViolationNode(final PmdRuleViolation violation) {
        this.violation = violation;
    }

    public PmdRuleViolation getViolation() {
        return violation;
    }

    @Override
    public int getViolationsCount() {
        return 1;
    }

    @Override
    public Set<PmdRulePriority> getRulePriorities() {
        return Collections.singleton(violation.getPmdRule().getPmdRulePriority());
    }

    @Override
    public boolean isLeaf() {
        return true;
    }
}
