package com.github.ybroeker.pmdidea.quickfix;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class UnnecessaryConstructorFix extends AbstractLocalQuickFix<PsiMethod> {

    @Override
    protected void applyFix(final @NotNull Project project, final @NotNull PsiMethod target) {
        target.delete();
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getFamilyName() {
        return "Removes unnecessary constructor";
    }
}
