package com.github.ybroeker.pmdidea.config;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

import javax.swing.event.SwingPropertyChangeSupport;

import com.github.ybroeker.pmdidea.pmd.PmdVersion;
import com.intellij.util.lang.JavaVersion;

public class PmdConfigurationModel {

    private final PropertyChangeSupport propertyChangeSupport = new SwingPropertyChangeSupport(this, true);

    private final PmdConfigurationService.State originalState;

    private String rulesPath = null;

    private boolean checkTests = false;

    private PmdVersion pmdVersion;

    private JavaVersion jdkVersion;

    public PmdConfigurationModel(PmdConfigurationService.State originalState) {
        this.originalState = originalState;
        reset();
    }

    public String getRulesPath() {
        return rulesPath;
    }

    public void setRulesPath(final String rulesPath) {
        String oldRulesPath = this.rulesPath;
        this.rulesPath = rulesPath;
        propertyChangeSupport.firePropertyChange("rulesPath", oldRulesPath, rulesPath);
    }

    public boolean isCheckTests() {
        return checkTests;
    }

    public void setCheckTests(final boolean checkTests) {
        boolean oldCheckTests = this.checkTests;
        this.checkTests = checkTests;
        propertyChangeSupport.firePropertyChange("checkTests", oldCheckTests, checkTests);

    }

    public PmdVersion getPmdVersion() {
        return pmdVersion;
    }

    public void setPmdVersion(final PmdVersion pmdVersion) {
        PmdVersion oldPmdVersion = this.pmdVersion;
        this.pmdVersion = pmdVersion;
        propertyChangeSupport.firePropertyChange("pmdVersion", oldPmdVersion, pmdVersion);

    }

    public JavaVersion getJdkVersion() {
        return jdkVersion;
    }

    public void setJdkVersion(final JavaVersion jdkVersion) {
        JavaVersion oldJdkVersion = this.jdkVersion;
        this.jdkVersion = jdkVersion;
        propertyChangeSupport.firePropertyChange("jdkVersion", oldJdkVersion, jdkVersion);

    }

    public PmdConfigurationService.State build() {
        return new PmdConfigurationService.State(this.rulesPath, this.checkTests, this.pmdVersion, this.jdkVersion);
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void reset() {
        this.setCheckTests(originalState.isCheckTests());
        this.setRulesPath(originalState.getRulesPath());
        this.setJdkVersion(originalState.getJdkVersion());
        this.setPmdVersion(originalState.getPmdVersion());
    }

    public boolean isModified() {
        final PmdConfigurationService.State newState = this.build();
        return !Objects.equals(originalState,newState);
    }
}
