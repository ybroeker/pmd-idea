package com.github.ybroeker.pmdidea.pmdwrapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;

import com.github.ybroeker.pmdidea.pmd.*;
import net.sourceforge.pmd.*;
import net.sourceforge.pmd.cache.AnalysisCache;
import net.sourceforge.pmd.lang.*;
import net.sourceforge.pmd.processor.*;
import net.sourceforge.pmd.util.datasource.DataSource;

import static java.util.Collections.singletonList;

public class PmdRunner implements Runnable {

    private final AnalysisCache analysisCache;

    private final RuleSetFactory ruleSetFactory;

    private final PmdConfiguration pmdConfiguration;

    public PmdRunner(final PmdConfiguration pmdConfiguration, final AnalysisCache analysisCache, final RuleSetFactory ruleSetFactory) {
        this.pmdConfiguration = pmdConfiguration;
        this.analysisCache = analysisCache;
        this.ruleSetFactory = ruleSetFactory;
    }

    private PMDConfiguration getPmdConfiguration() {
        final PMDConfiguration pmdConfig = new PMDConfiguration();

        try {
            pmdConfig.prependClasspath(pmdConfiguration.getAuxClassPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        pmdConfig.setAnalysisCache(analysisCache);

        final Language lang = LanguageRegistry.getLanguage("Java");
        final String type = pmdConfiguration.getPmdOptions().getTargetJdk();
        if (type != null) {
            final LanguageVersion srcType = lang.getVersion(type);
            if (srcType != null) {
                pmdConfig.getLanguageVersionDiscoverer().setDefaultLanguageVersion(srcType);
            }
        }

        pmdConfig.setRuleSets(pmdConfiguration.getRuleSets());

        return pmdConfig;
    }

    @Override
    public void run() {
        final ClassLoader original = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            final PMDConfiguration configuration = getPmdConfiguration();

            final PmdRunListener pmdRunListener = pmdConfiguration.getPmdRunListener();
            final PmdRenderer renderer = new PmdRenderer(pmdRunListener);

            renderer.setWriter(new NullWriter());
            renderer.start();

            final List<DataSource> dataSources = getDataSources();

            final RuleContext ctx = new RuleContext();

            ctx.getReport().addListener(configuration.getAnalysisCache());

            final AbstractPMDProcessor abstractPMDProcessor = getPmdProcessor(configuration);
            pmdRunListener.start(dataSources.size());
            abstractPMDProcessor.processFiles(ruleSetFactory, dataSources, ctx, singletonList(renderer));

            configuration.getAnalysisCache().persist();

            renderer.end();
            renderer.flush();
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }

    public AbstractPMDProcessor getPmdProcessor(final PMDConfiguration configuration) {
        AbstractPMDProcessor abstractPMDProcessor;
        if (pmdConfiguration.getFiles().size() > 1) {
            abstractPMDProcessor = new MultiThreadProcessor(configuration);
        } else {
            abstractPMDProcessor = new MonoThreadProcessor(configuration);
        }

        return abstractPMDProcessor;
    }

    public List<DataSource> getDataSources() {
        final List<DataSource> dataSources = new ArrayList<>(pmdConfiguration.getFiles().size());
        for (final ScannableFile file : pmdConfiguration.getFiles()) {
            dataSources.add(new ScannableFileDataSource(file));
        }

        dataSources.sort(Comparator.comparing(ds -> ds.getNiceFileName(false, "")));

        return dataSources;
    }

}
