package com.github.ybroeker.pmdidea.pmdwrapper;

import java.io.*;

import com.github.ybroeker.pmdidea.pmd.ScannableFile;
import net.sourceforge.pmd.util.datasource.DataSource;


//Closeable is required sind PMD 6.19.0
public class ScannableFileDataSource implements DataSource, Closeable {

    private final ScannableFile scannableFile;

    public ScannableFileDataSource(final ScannableFile scannableFile) {
        this.scannableFile = scannableFile;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return scannableFile.getInputStream();
    }

    @Override
    public String getNiceFileName(final boolean shortNames, final String inputFileName) {
        return scannableFile.getDisplayName();
    }

    @Override
    public void close() throws IOException {
        //Closeable is
    }
}
