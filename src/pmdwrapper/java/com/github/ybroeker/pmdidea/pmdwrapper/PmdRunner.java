package com.github.ybroeker.pmdidea.pmdwrapper;

import com.github.ybroeker.pmdidea.pmd.*;
import com.intellij.openapi.project.Project;
import net.sourceforge.pmd.*;
import net.sourceforge.pmd.lang.Language;
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.util.ResourceLoader;
import net.sourceforge.pmd.util.datasource.DataSource;
import net.sourceforge.pmd.util.datasource.FileDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

import static java.util.Collections.singletonList;

public class PmdRunner implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PmdRunner.class);

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

        Optional<File> cache = CacheFiles.getCacheFile(project);
        if (shouldUsePmdCache() && cache.isPresent()) {
            final String cacheFile = cache.get().getAbsolutePath();
            LOGGER.debug("PMD-Cache is used: {}", cacheFile);
            pmdConfig.setAnalysisCacheLocation(cacheFile);
        } else {
            LOGGER.debug("PMD-Cache is not used: shouldUseCache: {}, cacheFile: {}", shouldUsePmdCache(), cache);
        }

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

    /**
     * Checks if a scan should use the cache.
     * <p>
     * The cache should currently only be used when scanning more than one file.
     * This prevents individual file scans from overwriting a populated cache,
     * which would prevent the next full project scan from benefiting from it.
     *
     * @return if the scan should be used
     */
    private boolean shouldUsePmdCache() {
        return files.size() > 1;
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
