package com.github.ybroeker.pmdidea.quickfix;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

public class MakeLocalVariableFinalFix implements LocalQuickFix {

    private final String variableName;

    MakeLocalVariableFinalFix(String parameterName) {
        this.variableName = parameterName;
    }

    @NotNull
    @Override
    public String getName() {
        return "Make local variable '" + variableName + "' final";
    }

    @NotNull
    @Override
    public String getFamilyName() {
        return "Make variable 'final'";
    }

    @Override
    public void applyFix(@NotNull final Project project, @NotNull final ProblemDescriptor descriptor) {
        final PsiVariable variable = QuickfixFactory.findPsiElement(PsiVariable.class, descriptor.getEndElement());
        if (variable == null) {
            return;
        }
        applyFix(variable);
    }

    public void applyFix(final PsiVariable parameter) {
        final PsiModifierList modifierList = parameter.getModifierList();
        if (modifierList == null) {
            return;
        }
        modifierList.setModifierProperty(PsiModifier.FINAL, true);
    }

}
