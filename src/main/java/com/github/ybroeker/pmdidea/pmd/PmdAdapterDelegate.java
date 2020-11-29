package com.github.ybroeker.pmdidea.pmd;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;


@Service
public final class PmdAdapterDelegate implements PmdAdapter {

    private final Project project;

    private PmdAdapter delegate;

    public PmdAdapterDelegate(final Project project) {
        this.project = project;
    }


    @Override
    public PmdVersion getPmdVersion() {
        return delegate.getPmdVersion();
    }

    @Override
    public void runPmd(final PmdConfiguration pmdConfiguration) {
        if (delegate == null || !delegate.getPmdVersion().equals(pmdConfiguration.getPmdOptions().getPmdVersion())) {
            this.delegate = load(pmdConfiguration.getPmdOptions().getPmdVersion());
        }
        this.delegate.runPmd(pmdConfiguration);
    }

    @NotNull
    private PmdAdapter load(final PmdVersion version) {
        final PmdAdapterFactory service = project.getService(PmdAdapterFactory.class);
        return service.load(version);
    }

}
