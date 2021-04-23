package com.github.ybroeker.pmdidea.quickfix;

import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import static com.github.ybroeker.pmdidea.quickfix.PsiElements.findParentPsiElement;

public class UseDiamondOperatorFixFactory  implements QuickFixFactory {

    @Override
    public @NotNull List<LocalQuickFix> getQuickFix(final @NotNull PsiElement target, final @NotNull PmdRuleViolation violation) {
        if (violation.getPmdRule().getName().equals("UseDiamondOperator")) {
            PsiExpression element = findParentPsiElement(PsiExpression.class, target);
            if (element == null) {
                return Collections.emptyList();
            }
            return Collections.singletonList(new UseDiamondOperatorFix());
        }
        return Collections.emptyList();
    }

}
