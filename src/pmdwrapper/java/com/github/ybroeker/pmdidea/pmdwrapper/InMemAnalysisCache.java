package com.github.ybroeker.pmdidea.pmdwrapper;

import net.sourceforge.pmd.cache.AbstractAnalysisCache;

public class InMemAnalysisCache extends AbstractAnalysisCache  {

    @Override
    public void persist() {
        this.fileResultsCache.putAll(this.updatedResultsCache);
        this.updatedResultsCache.clear();
    }

    protected boolean cacheExists() {
        return true;
    }

}
