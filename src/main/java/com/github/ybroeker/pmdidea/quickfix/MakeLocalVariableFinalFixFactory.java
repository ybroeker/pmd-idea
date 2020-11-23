package com.github.ybroeker.pmdidea.quickfix;

import java.util.Collections;
import java.util.List;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiVariable;
import org.jetbrains.annotations.NotNull;

import static com.github.ybroeker.pmdidea.quickfix.PsiElements.findParentPsiElement;

public class MakeLocalVariableFinalFixFactory implements QuickFixFactory {

    @Override
    public @NotNull List<LocalQuickFix> getQuickFix(final @NotNull PsiElement target, final @NotNull PmdRuleViolation violation) {
        if (violation.getPmdRule().getName().equals("LocalVariableCouldBeFinal")) {
            PsiVariable variable = findParentPsiElement(PsiVariable.class, target);
            if (variable == null) {
                return Collections.emptyList();
            }
            return Collections.singletonList(new MakeLocalVariableFinalFix(variable.getName()));
        }
        return Collections.emptyList();
    }

}
