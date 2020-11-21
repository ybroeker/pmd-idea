package com.github.ybroeker.pmdidea.pmd;

import com.intellij.openapi.components.Service;

@Service
public final class PmdAdapterDelegate implements PmdAdapter {

    private PmdAdapter delegate;

    @Override
    public PmdVersion getPmdVersion() {
        return delegate.getPmdVersion();
    }

    @Override
    public void runPmd(final PmdConfiguration pmdConfiguration) {
        if (delegate == null || !delegate.getPmdVersion().equals(pmdConfiguration.getPmdOptions().getPmdVersion())) {
            delegate = PmdAdapterLoader.getInstance(pmdConfiguration.getPmdOptions().getPmdVersion());
        }

        delegate.runPmd(pmdConfiguration);
    }

}
