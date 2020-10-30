package com.github.ybroeker.pmdidea.config;

import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

import com.github.ybroeker.pmdidea.pmd.PmdVersion;
import com.intellij.util.xmlb.Converter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PmdVersionConverter extends Converter<PmdVersion> {

    @Nullable
    @Override
    public PmdVersion fromString(@NotNull final String value) {
        return PmdVersion.of(value);
    }

    @Override
    public @Nullable String toString(@NotNull final PmdVersion value) {
        return value.toString();

    }
}
