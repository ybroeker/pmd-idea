package com.github.ybroeker.pmdidea.pmd;

import java.io.IOException;
import java.io.Writer;
import java.lang.invoke.MethodHandles;
import java.util.Objects;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

public class NullWriter extends Writer {

    @Override
    public Writer append(final char character) throws IOException {
        return this;
    }

    @Override
    public Writer append(final CharSequence csq) throws IOException {
        return this;
    }

    @Override
    public Writer append(final CharSequence csq, final int start, final int end) throws IOException {
        return this;
    }

    @Override
    public void write(final int character) throws IOException {
    }

    @Override
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
    }

    @Override
    public void write(final @NotNull String str) throws IOException {
    }

    @Override
    public void write(final @NotNull String str, final int off, final  int len) throws IOException {
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws IOException {
    }

}
