package com.github.ybroeker.pmdidea.quickfix;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

public class MakeParameterFinalFix implements LocalQuickFix {

    private final String parameterName;

    public MakeParameterFinalFix(String parameterName) {
        this.parameterName = parameterName;
    }

    @NotNull
    @Override
    public String getName() {
        return "Make parameter '" + parameterName + "' final";
    }

    @NotNull
    @Override
    public String getFamilyName() {
        return "Make parameter 'final'";
    }

    @Override
    public void applyFix(@NotNull final Project project, @NotNull final ProblemDescriptor descriptor) {
        final PsiParameter parameter = QuickfixFactory.findParameter(descriptor.getPsiElement());
        if (parameter == null) {
            return;
        }
        applyFix(parameter);
    }

    public void applyFix(final PsiParameter parameter) {
        final PsiModifierList modifierList = parameter.getModifierList();
        if (modifierList == null) {
            return;
        }
        modifierList.setModifierProperty(PsiModifier.FINAL, true);
    }

}
