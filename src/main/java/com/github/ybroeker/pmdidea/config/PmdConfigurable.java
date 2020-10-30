package com.github.ybroeker.pmdidea.config;

import javax.swing.*;

import com.github.ybroeker.pmdidea.PmdBundle;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;


public class PmdConfigurable implements Configurable {

    private final Project project;

    private PmdConfigurationModel stateModel;

    public PmdConfigurable(final Project project) {
        this.project = project;
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return PmdBundle.getMessage("plugin.configuration-name");
    }

    @Override
    public @Nullable JComponent createComponent() {
        final PmdConfigurationService service = project.getService(PmdConfigurationService.class);
        this.stateModel = new PmdConfigurationModel(service.getState());
        return new PmdConfigurationPanel(stateModel);
    }

    @Override
    public boolean isModified() {
        return stateModel.isModified();
    }

    @Override
    public void apply() {
        final PmdConfigurationService service = project.getService(PmdConfigurationService.class);
        service.setState(stateModel.build());
    }

    @Override
    public void reset() {
        stateModel.reset();
    }

    @Override
    public void disposeUIResources() {
        this.stateModel = null;
    }
}
