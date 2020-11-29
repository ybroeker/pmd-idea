package com.github.ybroeker.pmdidea.pmd;

import java.io.InputStream;


class ScannableResource implements ScannableFile {

    private final String resourceName;

    public ScannableResource(final String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public String getDisplayName() {
        return resourceName;
    }

    @Override
    public InputStream getInputStream() {
        return ScannableResource.class.getResourceAsStream(resourceName);
    }
}
