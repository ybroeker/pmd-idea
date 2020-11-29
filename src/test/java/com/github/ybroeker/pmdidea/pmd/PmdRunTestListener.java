package com.github.ybroeker.pmdidea.pmd;

import java.util.ArrayList;
import java.util.List;


class PmdRunTestListener implements PmdRunListener {

    private int processFileCalled = 0;
    private boolean started = false;
    private int nrOfFiles = -1;
    private boolean finished = false;
    private List<PmdRuleViolation> pmdRuleViolations = new ArrayList<>();

    @Override
    public synchronized void processFile() {
        processFileCalled++;
    }

    @Override
    public synchronized void start(final int nrOfFiles) {
        this.nrOfFiles = nrOfFiles;
        this.started = true;
    }

    @Override
    public synchronized void finished() {
        this.finished = true;
    }

    @Override
    public synchronized void addViolation(final PmdRuleViolation ruleViolation) {
        pmdRuleViolations.add(ruleViolation);
    }

    public int getProcessFileCalled() {
        return processFileCalled;
    }

    public boolean isStarted() {
        return started;
    }

    public int getNrOfFiles() {
        return nrOfFiles;
    }

    public boolean isFinished() {
        return finished;
    }

    public List<PmdRuleViolation> getPmdRuleViolations() {
        return pmdRuleViolations;
    }
}
