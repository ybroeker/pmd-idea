package com.github.ybroeker.pmdidea.quickfix;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

public class MakeLocalVariableFinalFix extends AbstractLocalQuickFix<PsiVariable> {

    private final String variableName;

    MakeLocalVariableFinalFix(final String parameterName) {
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
    public void applyFix(@NotNull final Project project, @NotNull final PsiVariable variable) {
        final PsiModifierList modifierList = variable.getModifierList();
        if (modifierList == null) {
            return;
        }
        modifierList.setModifierProperty(PsiModifier.FINAL, true);
    }

}
