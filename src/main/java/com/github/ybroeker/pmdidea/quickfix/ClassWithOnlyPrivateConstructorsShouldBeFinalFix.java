package com.github.ybroeker.pmdidea.quickfix;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;


public class ClassWithOnlyPrivateConstructorsShouldBeFinalFix extends AbstractLocalQuickFix<PsiClass> {

    @Override
    protected void applyFix(final @NotNull Project project, final @NotNull PsiClass target) {
        final PsiModifierList modifierList = target.getModifierList();
        if (modifierList == null) {
            return;
        }
        modifierList.setModifierProperty(PsiModifier.FINAL, true);
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getFamilyName() {
        return "Make class final";
    }

}
