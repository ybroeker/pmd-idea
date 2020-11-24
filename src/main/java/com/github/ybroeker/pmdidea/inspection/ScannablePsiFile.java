package com.github.ybroeker.pmdidea.inspection;

import java.io.*;
import java.nio.charset.StandardCharsets;

import com.github.ybroeker.pmdidea.pmd.ScannableFile;
import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiFile;


public class ScannablePsiFile implements ScannableFile {

    private final PsiFile psiFile;

    public ScannablePsiFile(final PsiFile psiFile) {
        this.psiFile = psiFile;
    }

    @Override
    public String getDisplayName() {
        return psiFile.getVirtualFile().getPresentableUrl();
    }

    @Override
    public InputStream getInputStream() {
        final String text = ReadAction.compute(psiFile::getText);
        final byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        return new ByteArrayInputStream(bytes);
    }

}
