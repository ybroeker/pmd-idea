package com.github.ybroeker.pmdidea.pmdwrapper;

import com.github.ybroeker.pmdidea.pmd.*;
import com.intellij.openapi.project.Project;
import net.sourceforge.pmd.*;
import net.sourceforge.pmd.lang.Language;
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.util.datasource.DataSource;
import net.sourceforge.pmd.util.datasource.FileDataSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

public class PmdRunner implements Runnable {

    private final Project project;
    private final List<DataSource> files;
    private final String ruleSets;
    private final PmdOptions pmdOptions;

    private final PmdRunListener pmdRunListener;

    public PmdRunner(final Project project, final List<File> files, final String rule, final PmdRunListener pmdRunListener, final PmdOptions pmdOptions) {
        final List<DataSource> fileDataSources = new ArrayList<>(files.size());
        for (final File file : files) {
            fileDataSources.add(new FileDataSource(file));
        }
        this.project = project;
        this.files = fileDataSources;
        this.ruleSets = rule;
        this.pmdOptions = pmdOptions;
        this.pmdRunListener = pmdRunListener;
    }

    public PmdRunner(final PmdConfiguration pmdConfiguration) {
        this(pmdConfiguration.getProject(), pmdConfiguration.getFiles(), pmdConfiguration.getRuleSets(), pmdConfiguration.getPmdRunListener(), pmdConfiguration.getPmdOptions());
    }

    private PMDConfiguration getPmdConfiguration() {
        final PMDConfiguration pmdConfig = new PMDConfiguration();

        //TODO: classpath

        CacheFiles.getCacheFile(project).ifPresent(file -> pmdConfig.setAnalysisCacheLocation(file.getAbsolutePath()));

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

    @Override
    public void run() {
        final ClassLoader original = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            final PMDConfiguration pmdConfig = getPmdConfiguration();

            final RuleSetFactory ruleSetFactory = RulesetsFactoryUtils.createFactory(pmdConfig);

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
