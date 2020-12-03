package com.github.ybroeker.pmdidea.quickfix;

import java.util.*;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import static com.github.ybroeker.pmdidea.quickfix.PsiElements.findParentPsiElement;
import static com.github.ybroeker.pmdidea.quickfix.UseLocaleWithCaseConversionsFix.LocaleToUse.*;


public class UseLocaleWithCaseConversionsFixFactory implements QuickFixFactory {

    @Override
    public @NotNull List<LocalQuickFix> getQuickFix(@NotNull final PsiElement target, @NotNull final PmdRuleViolation violation) {
        if (!violation.getPmdRule().getName().equals("UseLocaleWithCaseConversions")) {
            return Collections.emptyList();
        }
        final PsiElement member = findParentPsiElement(PsiMethodCallExpression.class, target);
        if (member == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(new UseLocaleWithCaseConversionsFix(ROOT), new UseLocaleWithCaseConversionsFix(DEFAULT));
    }

}
