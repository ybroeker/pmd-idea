package com.github.ybroeker.pmdidea.pmdwrapper;

import com.github.ybroeker.pmdidea.pmd.*;
import net.sourceforge.pmd.PMDVersion;


@SuppressWarnings("unused")//Called by reflection
public class PmdWrapperImpl implements PmdAdapter {

    private final InMemAnalysisCache analysisCache = new InMemAnalysisCache();

    @Override
    public PmdVersion getPmdVersion() {
       return PmdVersion.of(PMDVersion.VERSION);
    }


    @Override
    public void runPmd(final PmdConfiguration pmdConfiguration) {
        final PmdRunner pmdRunner = new PmdRunner(pmdConfiguration, analysisCache);

        pmdRunner.run();
    }


}
