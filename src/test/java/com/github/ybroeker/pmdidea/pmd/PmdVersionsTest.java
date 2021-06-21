package com.github.ybroeker.pmdidea.pmd;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class PmdVersionsTest {

    @Test
    public void shouldHaveVersions() {
        assertThat(PmdVersions.getVersions())
                .isNotEmpty();
    }

    @Test
    public void shouldReturnLatestVersion() {
        assertThat(PmdVersions.getLatestVersion())
                .isEqualTo(PmdVersion.of("6.35.0"));
    }

}
