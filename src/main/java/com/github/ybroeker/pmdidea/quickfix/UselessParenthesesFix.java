package com.github.ybroeker.pmdidea.quickfix;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiParenthesizedExpression;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;


public class UselessParenthesesFix extends AbstractLocalQuickFix<PsiParenthesizedExpression> {

    private final String text;

    public UselessParenthesesFix(final String text) {
        this.text = text;
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getName() {
        return "Removes useless parentheses around '" + text + "'";
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getFamilyName() {
        return "Removes useless parentheses";
    }

    @Override
    public void applyFix(final Project project, @NotNull final PsiParenthesizedExpression target) {
        final PsiElement expression = target.getExpression();
        if (expression == null) {
            return;
        }

        target.replace(expression);
    }

}
