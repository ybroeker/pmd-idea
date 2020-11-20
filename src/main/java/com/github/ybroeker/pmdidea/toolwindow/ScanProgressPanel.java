package com.github.ybroeker.pmdidea.toolwindow;

import javax.swing.*;

import com.github.ybroeker.pmdidea.PmdBundle;

public class ScanProgressPanel extends Box {

    private static final long serialVersionUID = 1;

    private JProgressBar jProgressBar = new JProgressBar();

    private JLabel label = new JLabel(PmdBundle.message("scan.finished", 0, 0));

    private ScanProgressModel scanProgressModel = new ScanProgressModel(this);

    public ScanProgressPanel() {
        super(BoxLayout.LINE_AXIS);
        this.add(label);
        this.add(Box.createHorizontalGlue());
        this.add(jProgressBar);
    }

    public ScanProgressModel getScanProgressModel() {
        return scanProgressModel;
    }

    public void update() {
        if (scanProgressModel.isFinished()) {
            jProgressBar.setVisible(false);
            label.setText(PmdBundle.message("scan.finished", scanProgressModel.getFilesScanned(), scanProgressModel.getViolationsDetected()));
        } else {
            jProgressBar.setVisible(true);
            jProgressBar.setValue(scanProgressModel.getFilesScanned());
            jProgressBar.setMaximum(scanProgressModel.getFilesToScan());
            label.setText(PmdBundle.message("scan.progress", scanProgressModel.getFilesScanned(), scanProgressModel.getFilesToScan(), scanProgressModel.getViolationsDetected()));
        }
    }

}
