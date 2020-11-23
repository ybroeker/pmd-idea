package com.github.ybroeker.pmdidea.quickfix;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiParenthesizedExpression;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class UselessParenthesesFix implements LocalQuickFix {

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
    public void applyFix(@NotNull final Project project, @NotNull final ProblemDescriptor descriptor) {
        final PsiParenthesizedExpression parenthesizedExpression = QuickfixFactory.findPsiElement(PsiParenthesizedExpression.class, descriptor.getPsiElement());
        if (parenthesizedExpression == null) {
            return;
        }
        final PsiElement expression = parenthesizedExpression.getExpression();
        if (expression == null) {
            return;
        }

        parenthesizedExpression.replace(expression);
    }

    public void applyFix(@NotNull final PsiParenthesizedExpression parenthesizedExpression) {
        final PsiElement expression = parenthesizedExpression.getExpression();
        if (expression == null) {
            return;
        }

        parenthesizedExpression.replace(expression);
    }


}
