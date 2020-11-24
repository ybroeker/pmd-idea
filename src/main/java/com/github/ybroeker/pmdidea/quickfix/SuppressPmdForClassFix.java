package com.github.ybroeker.pmdidea.quickfix;

import com.intellij.codeInsight.daemon.impl.actions.SuppressFix;
import com.intellij.codeInsight.intention.LowPriorityAction;
import com.intellij.debugger.memory.utils.LowestPriorityCommand;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SuppressPmdForClassFix extends SuppressFix implements LowPriorityAction {

    public SuppressPmdForClassFix() {
        super("PMD");
    }

    @Override
    public @Nullable PsiJavaDocumentedElement getContainer(final PsiElement context) {
        final PsiJavaDocumentedElement container = super.getContainer(context);
        if (container == null) {
            return null;
        }
        return PsiElements.findParentPsiElement(PsiClass.class, container);
    }

    @Override
    public @NotNull String getText() {
        return "Suppress PMD for class";
    }

}
