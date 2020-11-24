package com.github.ybroeker.pmdidea.quickfix;

import java.util.*;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

public class SuppressPmdFixFactory implements QuickFixFactory {

    @Override
    public @NotNull List<LocalQuickFix> getQuickFix(@NotNull final PsiElement target, @NotNull final PmdRuleViolation violation) {
        final String ruleName = violation.getPmdRule().getName();

        final PsiJavaDocumentedElement parentPsiElement = PsiElements.findParentPsiElement(PsiJavaDocumentedElement.class, target);
        if (parentPsiElement instanceof PsiClass) {
            return Collections.singletonList(new SuppressPmdRuleForClassFix(ruleName));
        }

        return Arrays.asList(new SuppressPmdRuleForClassFix(ruleName), new SuppressPmdRuleFix(ruleName));
    }
}
