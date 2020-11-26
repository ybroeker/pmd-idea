package com.github.ybroeker.pmdidea.quickfix;

import java.util.Collections;
import java.util.List;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.siyeh.ig.fixes.AddSerialVersionUIDFix;
import org.jetbrains.annotations.NotNull;

public class MissingSerialVersionUIDFixFactory implements QuickFixFactory {

    @Override
    public @NotNull List<LocalQuickFix> getQuickFix(@NotNull final PsiElement target, @NotNull final PmdRuleViolation violation) {
        if (!violation.getPmdRule().getName().equals("MissingSerialVersionUID")) {
            return Collections.emptyList();
        }
        final PsiClass psiClass = PsiElements.findParentPsiElement(PsiClass.class, target);
        if (psiClass == null) {
            return Collections.emptyList();
        }

        return Collections.singletonList(new AddSerialVersionUIDFix());
    }

}
