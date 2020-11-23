package com.github.ybroeker.pmdidea.quickfix;

import java.util.Collections;
import java.util.List;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

import static com.github.ybroeker.pmdidea.quickfix.PsiElements.findParentPsiElement;

public class UnnecessaryConstructorFixFactory implements QuickFixFactory {

    @Override
    public @NotNull List<LocalQuickFix> getQuickFix(final @NotNull PsiElement target, final @NotNull PmdRuleViolation violation) {
        if (violation.getPmdRule().getName().equals("UnnecessaryConstructor")) {
            PsiMethod element = findParentPsiElement(PsiMethod.class, target);
            if (element == null) {
                return Collections.emptyList();
            }
            return Collections.singletonList(new UnnecessaryConstructorFix());
        }
        return Collections.emptyList();
    }

}
