package com.github.ybroeker.pmdidea.pmdwrapper;

import com.github.ybroeker.pmdidea.pmd.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.OrderEnumerator;
import net.sourceforge.pmd.*;
import net.sourceforge.pmd.cache.AnalysisCache;
import net.sourceforge.pmd.lang.Language;
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.util.ResourceLoader;
import net.sourceforge.pmd.util.datasource.DataSource;

import java.io.*;
import java.util.*;

import static java.util.Collections.singletonList;

public class PmdRunner implements Runnable {

    private final Project project;
    private final List<DataSource> files;
    private final String ruleSets;
    private final PmdOptions pmdOptions;

    private final PmdRunListener pmdRunListener;

    private final AnalysisCache analysisCache;

    private PmdRunner(final Project project, final List<ScannableFile> files, final String rule, final PmdRunListener pmdRunListener, final PmdOptions pmdOptions, final AnalysisCache analysisCache) {
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
    }

    public PmdRunner(final PmdConfiguration pmdConfiguration, final AnalysisCache analysisCache) {
        this(pmdConfiguration.getProject(), pmdConfiguration.getFiles(), pmdConfiguration.getRuleSets(), pmdConfiguration.getPmdRunListener(), pmdConfiguration.getPmdOptions(), analysisCache);
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
            final PMDConfiguration pmdConfig = getPmdConfiguration();

            final RuleSetFactory ruleSetFactory = RulesetsFactoryUtils.getRulesetFactory(pmdConfig, new ResourceLoader(this.getClass().getClassLoader()));

            final PmdRenderer renderer = new PmdRenderer(pmdRunListener);

            renderer.setWriter(new NullWriter());
            renderer.start();

            pmdRunListener.start(files.size());

            PMD.processFiles(pmdConfig, ruleSetFactory, files, new RuleContext(), singletonList(renderer));

            renderer.end();
            renderer.flush();
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }

}
