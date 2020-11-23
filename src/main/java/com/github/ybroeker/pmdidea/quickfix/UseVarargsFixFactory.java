package com.github.ybroeker.pmdidea.quickfix;

import java.util.Collections;
import java.util.List;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiParameter;
import org.jetbrains.annotations.NotNull;

import static com.github.ybroeker.pmdidea.quickfix.PsiElements.findParentPsiElement;

public class UseVarargsFixFactory  implements QuickFixFactory{

    @Override
    public @NotNull List<LocalQuickFix> getQuickFix(@NotNull final PsiElement target, @NotNull final PmdRuleViolation violation) {
        if (violation.getPmdRule().getName().equals("UseVarargs")) {
            PsiParameter parameter = findParentPsiElement(PsiParameter.class, target);
            if (parameter == null) {
                return Collections.emptyList();
            }
            return Collections.singletonList(new UseVarargsFix(parameter.getName()));
        }
        return Collections.emptyList();

    }

}
