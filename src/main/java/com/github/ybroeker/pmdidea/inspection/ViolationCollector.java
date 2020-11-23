package com.github.ybroeker.pmdidea.inspection;

import java.util.*;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.github.ybroeker.pmdidea.pmd.PmdRunListener;

import static java.util.Collections.unmodifiableList;


public class ViolationCollector implements PmdRunListener {

    List<PmdRuleViolation> violations = new ArrayList<>();

    @Override
    public void processFile() {
    }

    @Override
    public void start(final int nrOfFiles) {
    }

    @Override
    public void finished() {
    }

    @Override
    public void addViolation(final PmdRuleViolation ruleViolation) {
        violations.add(ruleViolation);
    }

    public List<PmdRuleViolation> getViolations() {
        return unmodifiableList(violations);
    }
}
