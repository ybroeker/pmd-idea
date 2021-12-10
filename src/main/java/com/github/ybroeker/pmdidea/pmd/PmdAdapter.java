package com.github.ybroeker.pmdidea.pmd;


import java.util.List;

public interface PmdAdapter {

    PmdVersion getPmdVersion();

    List<String> getRulesetNames(PmdConfiguration pmdConfiguration);

    void runPmd(PmdConfiguration pmdConfiguration);

}
