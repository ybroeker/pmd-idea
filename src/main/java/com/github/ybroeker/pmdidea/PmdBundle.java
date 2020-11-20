package com.github.ybroeker.pmdidea;

import com.intellij.AbstractBundle;
import org.jetbrains.annotations.*;


public final class PmdBundle extends AbstractBundle {

    @NonNls
    private static final String BUNDLE = "com.github.ybroeker.pmdidea.PmdBundle";

    private static final PmdBundle INSTANCE = new PmdBundle();

    private PmdBundle() {
        super(BUNDLE);
    }


    public static String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, @NotNull Object... params) {
        return INSTANCE.getMessage(key, params);
    }

}
