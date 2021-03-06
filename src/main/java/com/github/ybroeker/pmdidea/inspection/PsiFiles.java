package com.github.ybroeker.pmdidea.inspection;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public final class PsiFiles {

    private PsiFiles() {
    }

    public static @Nullable PsiElement getElement(@NotNull final PsiFile wrapped, final int line, final int column) {
        final int[] offsets = createLineOffsets(wrapped);

        if (line < 0 || line >= offsets.length) {
            return null;
        }

        final int offset = offsets[line] + column - 1;

        return wrapped.findElementAt(offset);
    }

    private static int[] createLineOffsets(@NotNull final PsiFile wrapped) {
        final String text = wrapped.getText();
        final String[] lines = text.split("\n");

        final int[] offsets = new int[lines.length + 1];
        offsets[0] = 0;

        int currentOffset = 0;
        for (int line = 0; line < lines.length; line++) {
            //PMDs line-nrs start at 1
            offsets[line + 1] = currentOffset;
            //add 1 sind linebreak were removed at text.split
            currentOffset += lines[line].length() + 1;
        }
        return offsets;
    }

}
