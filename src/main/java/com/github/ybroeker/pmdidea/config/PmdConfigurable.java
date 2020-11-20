package com.github.ybroeker.pmdidea.config;

import javax.swing.*;

import com.github.ybroeker.pmdidea.PmdBundle;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;


public class PmdConfigurable implements Configurable {

    private final Project project;

    private PmdConfigurationPanel pmdConfigurationPanel;

    public PmdConfigurable(final Project project) {
        this.project = project;
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return PmdBundle.message("plugin.configuration-name");
    }

    @Override
    public @Nullable JComponent createComponent() {
        this.pmdConfigurationPanel = new PmdConfigurationPanel(project);
        return pmdConfigurationPanel;
    }

    @Override
    public boolean isModified() {
        return pmdConfigurationPanel.isModified();
    }

    @Override
    public void apply() {
        pmdConfigurationPanel.apply();
    }

    @Override
    public void reset() {
        pmdConfigurationPanel.reset();
    }

}
