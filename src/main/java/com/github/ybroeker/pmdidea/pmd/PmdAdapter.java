package com.github.ybroeker.pmdidea.pmd;


public interface PmdAdapter {

    String getPmdVersion();

    void runPmd(PmdConfiguration pmdConfiguration);

}
