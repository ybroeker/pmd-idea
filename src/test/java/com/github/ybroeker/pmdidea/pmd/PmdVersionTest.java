package com.github.ybroeker.pmdidea.pmd;

import org.junit.Assert;
import org.junit.Test;


public class PmdVersionTest {

    @Test
    public void shouldParseVersion() {
        String versionString = "6.29.0";

        final PmdVersion version = PmdVersion.of(versionString);

        Assert.assertEquals(6, version.getMajor());
        Assert.assertEquals(29, version.getMinor());
        Assert.assertEquals(0, version.getPatch());
    }

    @Test
    public void shouldFormatVersion() {
        final PmdVersion version = new PmdVersion(6, 29, 0);

        Assert.assertEquals("6.29.0", version.toString());
    }

    @Test
    public void shouldSortVersions() {
        PmdVersion six = PmdVersion.of("6.0.0");
        PmdVersion sixPatched = PmdVersion.of("6.0.1");
        PmdVersion sixOne = PmdVersion.of("6.1.0");

        Assert.assertEquals(0, six.compareTo(six));
        Assert.assertEquals(-1, six.compareTo(sixPatched));
        Assert.assertEquals(-1, six.compareTo(sixOne));

        Assert.assertEquals(1, sixPatched.compareTo(six));
        Assert.assertEquals(0, sixPatched.compareTo(sixPatched));
        Assert.assertEquals(-1, sixPatched.compareTo(sixOne));

        Assert.assertEquals(1, sixOne.compareTo(six));
        Assert.assertEquals(1, sixOne.compareTo(sixPatched));
        Assert.assertEquals(0, sixOne.compareTo(sixOne));

    }
}
