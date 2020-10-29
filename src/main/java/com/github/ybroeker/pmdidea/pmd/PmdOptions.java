package com.github.ybroeker.pmdidea.pmd;


public class PmdOptions {

    private final String targetJdk;

    private final String pmdVersion;

    public PmdOptions(final String targetJdk, final String pmdVersion) {
        this.targetJdk = targetJdk;
        this.pmdVersion = pmdVersion;
    }

    public String getTargetJdk() {
        return targetJdk;
    }

    public String getPmdVersion() {
        return pmdVersion;
    }
}
