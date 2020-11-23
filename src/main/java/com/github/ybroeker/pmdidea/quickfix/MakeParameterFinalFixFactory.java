package com.github.ybroeker.pmdidea.quickfix;

import java.util.Collections;
import java.util.List;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiParameter;
import org.jetbrains.annotations.NotNull;

import static com.github.ybroeker.pmdidea.quickfix.PsiElements.findParentPsiElement;


public class MakeParameterFinalFixFactory implements QuickFixFactory {

    @Override
    public @NotNull List<LocalQuickFix> getQuickFix(final @NotNull PsiElement target, final @NotNull PmdRuleViolation violation) {
        if (violation.getPmdRule().getName().equals("MethodArgumentCouldBeFinal")) {
            PsiParameter parameter = findParentPsiElement(PsiParameter.class, target);
            if (parameter == null) {
                return Collections.emptyList();
            }
            return Collections.singletonList(new MakeParameterFinalFix(parameter.getName()));
        }
        return Collections.emptyList();
    }
}
