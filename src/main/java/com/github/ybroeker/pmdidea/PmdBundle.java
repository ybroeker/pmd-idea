package com.github.ybroeker.pmdidea;

import java.util.ResourceBundle;

import com.intellij.CommonBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

public final class PmdBundle {

    @NonNls
    private static final String BUNDLE = "com.github.ybroeker.pmdidea.PmdBundle";

    private PmdBundle() {
    }

    public static String getMessage(@PropertyKey(resourceBundle = BUNDLE) final String key, final Object... params) {
        return CommonBundle.message(getBundle(), key, params);
    }

    private static ResourceBundle getBundle() {
        return InstanceHolder.RESOURCE_BUNDLE;
    }

    static class InstanceHolder {
        static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE);
    }

}
