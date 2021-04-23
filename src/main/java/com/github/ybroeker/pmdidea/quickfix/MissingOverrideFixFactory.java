package com.github.ybroeker.pmdidea.quickfix;

import java.util.Collections;
import java.util.List;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import static com.github.ybroeker.pmdidea.quickfix.PsiElements.findParentPsiElement;


public class MissingOverrideFixFactory implements QuickFixFactory {

    @Override
    public @NotNull List<LocalQuickFix> getQuickFix(final @NotNull PsiElement target, final @NotNull PmdRuleViolation violation) {
        if (violation.getPmdRule().getName().equals("MissingOverride")) {
            PsiMethod method = findParentPsiElement(PsiMethod.class, target);
            if (method == null) {
                return Collections.emptyList();
            }
            return Collections.singletonList(new MissingOverrideFix());
        }
        return Collections.emptyList();
    }
}
