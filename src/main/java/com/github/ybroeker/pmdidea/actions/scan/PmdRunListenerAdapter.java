package com.github.ybroeker.pmdidea.actions.scan;

import java.util.*;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.github.ybroeker.pmdidea.toolwindow.PmdToolPanel;
import com.github.ybroeker.pmdidea.toolwindow.ScanProgressModel;
import com.github.ybroeker.pmdidea.pmd.PmdRunListener;

public class PmdRunListenerAdapter implements PmdRunListener {

    private final PmdToolPanel scan;

    private final ScanProgressModel scanProgressModel;

    private final List<PmdRuleViolation> ruleViolations = Collections.synchronizedList(new ArrayList<>());

    public PmdRunListenerAdapter(final PmdToolPanel scan) {
        this.scan = scan;
        this.scanProgressModel = scan.getScanProgress().getScanProgressModel();
    }

    @Override
    public void processFile() {
        scanProgressModel.incrementFilesScanned(1);
    }

    @Override
    public void start(final int nrOfFiles) {
        scanProgressModel.startScan(nrOfFiles);
    }

    @Override
    public void finished() {
        scanProgressModel.finishScan();
        scan.displayViolations(ruleViolations);
    }

    @Override
    public void addViolation(final PmdRuleViolation ruleViolation) {
        ruleViolations.add(ruleViolation);
        scanProgressModel.incrementViolationsFound(1);
    }

}
