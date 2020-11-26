package com.github.ybroeker.pmdidea.quickfix;

import java.util.Collections;
import java.util.List;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

import static com.github.ybroeker.pmdidea.quickfix.PsiElements.findParentPsiElement;

public class CallSuperInConstructorFixFactory implements QuickFixFactory {

    @Override
    public @NotNull List<LocalQuickFix> getQuickFix(@NotNull final PsiElement target, @NotNull final PmdRuleViolation violation) {
        if (!violation.getPmdRule().getName().equals("CallSuperInConstructor")) {
            return Collections.emptyList();
        }

        final PsiMethod method = findParentPsiElement(PsiMethod.class, target);
        if (method == null || !method.isConstructor()) {
            return Collections.emptyList();
        }

        return Collections.singletonList(new CallSuperInConstructorFix());
    }

}
