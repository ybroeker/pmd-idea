package com.github.ybroeker.pmdidea.quickfix;

import com.intellij.codeInsight.daemon.impl.actions.SuppressFix;
import com.intellij.codeInsight.intention.LowPriorityAction;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SuppressPmdRuleForClassFix extends SuppressFix implements LowPriorityAction {

    private final String ruleName;

    public SuppressPmdRuleForClassFix(@NotNull final String ruleName) {
        super("PMD." + ruleName);
        this.ruleName = ruleName;
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
        return "Suppress " + ruleName + " for class";
    }

    @Override
    public @NotNull String getFamilyName() {
        return "Suppress for class";
    }
}
