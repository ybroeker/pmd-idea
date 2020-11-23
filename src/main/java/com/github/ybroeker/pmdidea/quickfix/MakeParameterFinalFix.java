package com.github.ybroeker.pmdidea.quickfix;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

public class MakeParameterFinalFix extends AbstractLocalQuickFix<PsiParameter> {

    private final String parameterName;

    public MakeParameterFinalFix(final String parameterName) {
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
    protected void applyFix(@NotNull final Project project, @NotNull final PsiParameter parameter) {
        final PsiModifierList modifierList = parameter.getModifierList();
        if (modifierList == null) {
            return;
        }
        modifierList.setModifierProperty(PsiModifier.FINAL, true);
    }

}
