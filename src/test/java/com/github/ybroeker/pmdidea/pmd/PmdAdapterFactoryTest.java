package com.github.ybroeker.pmdidea.pmd;

import java.io.File;
import java.util.Collections;
import java.util.List;

import com.github.ybroeker.pmdidea.test.*;
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;


@IdeaProjectTest
@ExtendWith(ResourceResolver.class)
public class PmdAdapterFactoryTest {

    private final IdeaProjectTestFixture fixture;

    private final File rulesFile;

    public PmdAdapterFactoryTest(final IdeaProjectTestFixture fixture,
                                 @Resource("/pmd-rules.xml") final File rulesFile) {
        this.fixture = fixture;
        this.rulesFile = rulesFile;
    }

    @ParameterizedTest(name = "version: {0}")
    @MethodSource("testLoadPmdAdapter")
    public void testLoadPmdAdapter(final PmdVersion version) {
        final String rules = rulesFile.getAbsolutePath();

        final PmdAdapterFactory pmdAdapterFactory = new PmdAdapterFactory(fixture.getProject());

        final PmdAdapter pmdAdapter = pmdAdapterFactory.load(version);

        final PmdRunTestListener pmdRunTestListener = new PmdRunTestListener();
        final PmdOptions pmdOptions = new PmdOptions("1.8", version);
        final PmdConfiguration configuration = new PmdConfiguration(fixture.getProject(), Collections.singletonList(new ScannableResource("/TestClass.java")), rules, pmdOptions, pmdRunTestListener, null);

        pmdAdapter.runPmd(configuration);

        Assertions.assertThat(pmdRunTestListener.isFinished()).isTrue();
        Assertions.assertThat(pmdRunTestListener.isStarted()).isTrue();
        Assertions.assertThat(pmdRunTestListener.getPmdRuleViolations()).isNotEmpty();
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private static List<PmdVersion> testLoadPmdAdapter() {
        return PmdVersions.getVersions();
    }

}
