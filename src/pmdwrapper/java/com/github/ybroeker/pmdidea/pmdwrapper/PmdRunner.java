package com.github.ybroeker.pmdidea.pmdwrapper;

import java.io.*;
import java.util.*;

import com.github.ybroeker.pmdidea.pmd.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.OrderEnumerator;
import net.sourceforge.pmd.*;
import net.sourceforge.pmd.cache.AnalysisCache;
import net.sourceforge.pmd.lang.*;
import net.sourceforge.pmd.processor.*;
import net.sourceforge.pmd.util.datasource.DataSource;

import static java.util.Collections.singletonList;

public class PmdRunner implements Runnable {

    private final Project project;
    private final List<DataSource> files;
    private final String ruleSets;
    private final PmdOptions pmdOptions;

    private final PmdRunListener pmdRunListener;

    private final AnalysisCache analysisCache;

    private final RuleSetFactory ruleSetFactory;

    private PmdRunner(final Project project, final List<ScannableFile> files, final String rule, final PmdRunListener pmdRunListener, final PmdOptions pmdOptions, final AnalysisCache analysisCache, final RuleSetFactory ruleSetFactory) {
        final List<DataSource> fileDataSources = new ArrayList<>(files.size());
        for (final ScannableFile file : files) {
            fileDataSources.add(new ScannableFileDataSource(file));
        }
        this.project = project;
        this.files = fileDataSources;
        this.ruleSets = rule;
        this.pmdOptions = pmdOptions;
        this.pmdRunListener = pmdRunListener;
        this.analysisCache = analysisCache;
        this.ruleSetFactory = ruleSetFactory;
    }

    public PmdRunner(final PmdConfiguration pmdConfiguration, final AnalysisCache analysisCache, final RuleSetFactory ruleSetFactory) {
        this(pmdConfiguration.getProject(), pmdConfiguration.getFiles(), pmdConfiguration.getRuleSets(), pmdConfiguration.getPmdRunListener(), pmdConfiguration.getPmdOptions(), analysisCache, ruleSetFactory);
    }

    private PMDConfiguration getPmdConfiguration() {
        final PMDConfiguration pmdConfig = new PMDConfiguration();

        try {
            pmdConfig.prependClasspath(getClassPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        pmdConfig.setAnalysisCache(analysisCache);

        final Language lang = LanguageRegistry.getLanguage("Java");
        final String type = pmdOptions.getTargetJdk();
        if (type != null) {
            final LanguageVersion srcType = lang.getVersion(type);
            if (srcType != null) {
                pmdConfig.getLanguageVersionDiscoverer().setDefaultLanguageVersion(srcType);
            }
        }

        pmdConfig.setRuleSets(ruleSets);

        return pmdConfig;
    }

    private String getClassPath() {
        final Module[] modules = ModuleManager.getInstance(project).getModules();
        final StringJoiner joiner = new StringJoiner(File.pathSeparator);
        for (final Module module : modules) {
            joiner.add(OrderEnumerator.orderEntries(module).recursively().getPathsList().getPathsString());
        }
        return joiner.toString();
    }

    @Override
    public void run() {
        final ClassLoader original = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            final PMDConfiguration configuration = getPmdConfiguration();

            final PmdRenderer renderer = new PmdRenderer(pmdRunListener);

            renderer.setWriter(new NullWriter());
            renderer.start();


            final List<DataSource> dataSources = new ArrayList<>(files);
            dataSources.sort(Comparator.comparing(ds -> ds.getNiceFileName(false, "")));

            final RuleContext ctx = new RuleContext();

            ctx.getReport().addListener(configuration.getAnalysisCache());

            pmdRunListener.start(files.size());
            AbstractPMDProcessor abstractPMDProcessor;
            if (dataSources.size() > 1) {
                abstractPMDProcessor = new MultiThreadProcessor(configuration);
            } else {
                abstractPMDProcessor = new MonoThreadProcessor(configuration);
            }
            abstractPMDProcessor.processFiles(ruleSetFactory, dataSources, ctx, singletonList(renderer));


            // Persist the analysis cache
            configuration.getAnalysisCache().persist();

            renderer.end();
            renderer.flush();
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }

}
