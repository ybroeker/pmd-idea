package com.github.ybroeker.pmdidea.pmd;

import java.util.*;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import net.sourceforge.pmd.*;
import net.sourceforge.pmd.lang.Language;
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.util.datasource.DataSource;
import net.sourceforge.pmd.util.datasource.FileDataSource;
import org.jetbrains.annotations.NotNull;

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

    public PmdRunner(final Project project, final List<File> files, final String rule, final PmdRunListener pmdRunListener) {
        final List<DataSource> fileDataSources = new ArrayList<>(files.size());
        for (final File file : files) {
            fileDataSources.add(new FileDataSource(file));
        }
        this.project = project;
        this.files = fileDataSources;
        this.ruleSets = rule;
        this.pmdOptions = new PmdOptions("1.8");
        this.pmdRunListener = pmdRunListener;
    }

    private PMDConfiguration getPmdConfiguration() {
        final PMDConfiguration pmdConfig = new PMDConfiguration();

        //TODO: classpath

        getTemporaryFile().ifPresent(file -> pmdConfig.setAnalysisCacheLocation(file.getAbsolutePath()));

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

    public void runPmdInternal() {
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

    @Override
    public void run() {
        runPmdInternal();
    }


    @NotNull
    private Optional<File> getTemporaryFile() {
        return getCacheFolder().map(folder -> new File(folder, "pmd.tmp"));
    }

    private Optional<File> getCacheFolder() {
        if (project.getProjectFile() != null) {
            final VirtualFile possibleIdeaFolder = project.getProjectFile().getParent();
            return Optional.of(new File(possibleIdeaFolder.getPath()));
        }
        if (project.getBasePath() != null) {
            final String basePath = project.getBasePath();
            return Optional.of(new File(basePath));
        }
        return Optional.empty();
    }

}
