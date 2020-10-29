package com.github.ybroeker.pmdidea.pmdwrapper;

import com.github.ybroeker.pmdidea.pmd.PmdAdapter;
import com.github.ybroeker.pmdidea.pmd.PmdConfiguration;
import net.sourceforge.pmd.PMDVersion;


@SuppressWarnings("unused")//Called by reflection
public class PmdWrapperImpl implements PmdAdapter {

    @Override
    public String getPmdVersion() {
       return PMDVersion.VERSION;
    }


    @Override
    public void runPmd(final PmdConfiguration pmdConfiguration) {
        final PmdRunner pmdRunner = new PmdRunner(pmdConfiguration);

        pmdRunner.run();
    }


}
