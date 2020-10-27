package com.github.ybroeker.pmdidea.toolwindow;


public class ScanProgressModel {

    private int filesToScan;
    private int filesScanned;
    private  int violationsDetected;
    private boolean finished = false;
    private boolean started = false;

    private final ScanProgressPanel scanProgressPanel;

    public ScanProgressModel(final ScanProgressPanel scanProgressPanel) {

        this.scanProgressPanel = scanProgressPanel;
    }


    public void startScan(final int filesToScan) {
        started = true;
        finished = false;
        this.filesScanned = 0;
        this.filesToScan = filesToScan;
        this.violationsDetected = 0;

        scanProgressPanel.update();
    }

    public void finishScan() {
        this.finished = true;
        this.filesScanned = filesToScan;

        scanProgressPanel.update();
    }


    public void incrementViolationsFound(final int violationsDetected) {
        this.violationsDetected += violationsDetected;

        scanProgressPanel.update();
    }
    public void incrementFilesScanned(final int filesScanned) {
        this.filesScanned += filesScanned;

        scanProgressPanel.update();
    }


    public boolean isStarted() {
        return started;
    }

    public int getFilesToScan() {
        return filesToScan;
    }

    public int getFilesScanned() {
        return filesScanned;
    }

    public int getViolationsDetected() {
        return violationsDetected;
    }

    public boolean isFinished() {
        return finished;
    }
}
