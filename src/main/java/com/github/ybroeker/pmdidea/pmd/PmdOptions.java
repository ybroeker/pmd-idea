package com.github.ybroeker.pmdidea.pmd;


public class PmdOptions {

    private final String targetJdk;

    private final PmdVersion pmdVersion;

    public PmdOptions(final String targetJdk, final PmdVersion pmdVersion) {
        this.targetJdk = targetJdk;
        this.pmdVersion = pmdVersion;
    }

    public String getTargetJdk() {
        return targetJdk;
    }

    public PmdVersion getPmdVersion() {
        return pmdVersion;
    }
}
