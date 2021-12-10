package com.github.ybroeker.pmdidea.pmdwrapper;

import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;
import java.util.stream.*;

import com.github.ybroeker.pmdidea.pmd.*;
import net.sourceforge.pmd.*;
import net.sourceforge.pmd.util.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("unused")//Called by reflection
public class PmdWrapperImpl implements PmdAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PmdWrapperImpl.class);

    private final InMemAnalysisCache analysisCache = new InMemAnalysisCache();

    private final ResourceLoader resourceLoader;

    private CachingRuleSetFactory cachingRuleSetFactory;

    public PmdWrapperImpl() {
        this.resourceLoader = new ResourceLoader(this.getClass().getClassLoader());
    }

    @Override
    public PmdVersion getPmdVersion() {
        return PmdVersion.of(PMDVersion.VERSION);
    }

    @Override
    public List<String> getRulesetNames(final PmdConfiguration pmdConfiguration) {
        try {

            final Iterator<RuleSet> registeredRuleSets = requireRuleset(pmdConfiguration).getRegisteredRuleSets();
            return streamOf(registeredRuleSets)
                    .map(RuleSet::getName)
                    .collect(Collectors.toList());

        } catch (RuleSetNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> Stream<T> streamOf(Iterator<T> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
    }

    @Override
    public void runPmd(final PmdConfiguration pmdConfiguration) {
        requireRuleset(pmdConfiguration);

        final PmdRunner pmdRunner = new PmdRunner(pmdConfiguration, analysisCache, cachingRuleSetFactory);

        pmdRunner.run();
    }

    private CachingRuleSetFactory requireRuleset(final PmdConfiguration pmdConfiguration) {
        final int rulesHash = getRulesHash(pmdConfiguration);
        if (cachingRuleSetFactory == null || !cachingRuleSetFactory.hasHash(rulesHash)) {
            LOGGER.debug("Create new cachingRuleSetFactory with hash '{}', existing is '{}'", rulesHash, cachingRuleSetFactory);
            cachingRuleSetFactory = new CachingRuleSetFactory(resourceLoader, rulesHash);
        }
        return cachingRuleSetFactory;
    }

    private int getRulesHash(final PmdConfiguration pmdConfiguration) {
        if (!Files.exists(Paths.get(pmdConfiguration.getRuleSets()))) {
            return -1;
        }

        final MessageDigest md5 = getMD5();

        try (InputStream inputStream = new DigestInputStream(new BufferedInputStream(Files.newInputStream(Paths.get(pmdConfiguration.getRuleSets()), StandardOpenOption.READ)), md5);) {
            readStream(inputStream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return Arrays.hashCode(md5.digest());
    }

    /**
     * Reads the whole stream, ignoring it's content.
     * @param inputStream the stream to read
     * @throws IOException if {@link InputStream#read(byte[])} throws an exception
     */
    @SuppressWarnings( {"PMD.EmptyWhileStmt", "StatementWithEmptyBody"})
    private void readStream(final InputStream inputStream) throws IOException {
        final byte[] buffer = new byte[1024];
        while (inputStream.read(buffer) == 1024) {
            //read stream to update message digest
        }
    }

    private static MessageDigest getMD5() {
        try {
            return MessageDigest.getInstance("MD5"); // lgtm [java/weak-cryptographic-algorithm]
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError("MD5 not available", e);
        }
    }

}
