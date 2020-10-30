package com.github.ybroeker.pmdidea.config;

import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

import com.intellij.util.lang.JavaVersion;
import com.intellij.util.xmlb.Converter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JavaVersionConverter extends Converter<JavaVersion> {

    @Override
    public @Nullable JavaVersion fromString(@NotNull final String value) {
        return JavaVersion.parse(value);
    }

    @Override
    public @Nullable String toString(@NotNull final JavaVersion value) {
        return value.toString();
    }
}
