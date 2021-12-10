package com.github.ybroeker.pmdidea.pmd;

import java.util.List;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public final class PmdAdapterDelegate implements PmdAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PmdAdapterDelegate.class);

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
    public List<String> getRulesetNames(final PmdConfiguration pmdConfiguration) {
        return requireValidDelegate(pmdConfiguration).getRulesetNames(pmdConfiguration);
    }

    @Override
    public void runPmd(final PmdConfiguration pmdConfiguration) {
        PmdAdapter pmdAdapter = requireValidDelegate(pmdConfiguration);
        LOGGER.debug("Run PMD with rulesets ({})", getRulesetNames(pmdConfiguration));
        pmdAdapter.runPmd(pmdConfiguration);
    }

    @NotNull
    private PmdAdapter load(final PmdVersion version) {
        final PmdAdapterFactory service = project.getService(PmdAdapterFactory.class);
        return service.load(version);
    }

    private PmdAdapter requireValidDelegate(final PmdConfiguration pmdConfiguration) {
        if (delegate == null || !delegate.getPmdVersion().equals(pmdConfiguration.getPmdOptions().getPmdVersion())) {
            this.delegate = load(pmdConfiguration.getPmdOptions().getPmdVersion());
        }
        return delegate;
    }

}
