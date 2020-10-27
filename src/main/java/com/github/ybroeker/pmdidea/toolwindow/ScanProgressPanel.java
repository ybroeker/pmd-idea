package com.github.ybroeker.pmdidea.toolwindow;

import javax.swing.*;

public class ScanProgressPanel extends Box {

    private static final long serialVersionUID = 1;

    private JProgressBar jProgressBar = new JProgressBar();
    private JLabel label = new JLabel("Scanning 0 files");

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
            label.setText(String.format("Scanned %d of %d, found %d violations", scanProgressModel.getFilesScanned(), scanProgressModel.getFilesToScan(), scanProgressModel.getViolationsDetected()));
        } else {
            jProgressBar.setVisible(true);
            jProgressBar.setValue(scanProgressModel.getFilesScanned());
            jProgressBar.setMaximum(scanProgressModel.getFilesToScan());
            label.setText(String.format("Scan %d of %d, found %d violations", scanProgressModel.getFilesScanned(), scanProgressModel.getFilesToScan(), scanProgressModel.getViolationsDetected()));
        }
    }

}
