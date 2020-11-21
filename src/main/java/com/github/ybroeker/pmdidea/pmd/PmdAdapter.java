package com.github.ybroeker.pmdidea.pmd;


public interface PmdAdapter {

    PmdVersion getPmdVersion();

    void runPmd(PmdConfiguration pmdConfiguration);

}
