package com.github.ybroeker.pmdidea.inspection;

import java.io.*;
import java.nio.charset.StandardCharsets;

import com.github.ybroeker.pmdidea.pmd.ScannableFile;
import com.intellij.psi.PsiFile;


public class VirtuallyFile implements ScannableFile {

    private final PsiFile psiFile;

    public VirtuallyFile(final PsiFile psiFile) {
        this.psiFile = psiFile;
    }

    @Override
    public String getDisplayName() {
        return psiFile.getName();
    }

    @Override
    public InputStream getInputStream() {
        final byte[] bytes = psiFile.getText().getBytes(StandardCharsets.UTF_8);
        return new ByteArrayInputStream(bytes);
    }

}
