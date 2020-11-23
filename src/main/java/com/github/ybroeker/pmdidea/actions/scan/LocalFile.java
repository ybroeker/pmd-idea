package com.github.ybroeker.pmdidea.actions.scan;

import java.io.*;
import java.nio.file.Files;

import com.github.ybroeker.pmdidea.pmd.ScannableFile;

public class LocalFile implements ScannableFile {

    private final File file;

    public LocalFile(final File file) {
        this.file = file;
    }

    @Override
    public String getDisplayName() {
        return file.getPath();
    }

    @Override
    public InputStream getInputStream() {
        try {
            return Files.newInputStream(file.toPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
