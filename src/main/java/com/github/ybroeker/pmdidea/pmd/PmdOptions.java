package com.github.ybroeker.pmdidea.pmd;


public class PmdOptions {

    private final String targetJdk;

    public PmdOptions(final String targetJdk) {
        this.targetJdk = targetJdk;
    }

    public String getTargetJdk() {
        return targetJdk;
    }
}
