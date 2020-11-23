package com.github.ybroeker.pmdidea.quickfix;

import java.util.Collections;
import java.util.List;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiParenthesizedExpression;
import org.jetbrains.annotations.NotNull;

import static com.github.ybroeker.pmdidea.quickfix.PsiElements.findParentPsiElement;

public class UselessParenthesesFixFactory implements QuickFixFactory {

    @Override
    public @NotNull List<LocalQuickFix> getQuickFix(@NotNull final PsiElement target, @NotNull final PmdRuleViolation violation) {
        if (violation.getPmdRule().getName().equals("UselessParentheses")) {
            PsiParenthesizedExpression member = findParentPsiElement(PsiParenthesizedExpression.class, target);
            if (member == null) {
                return Collections.emptyList();
            }
            if (member.getExpression() == null) {
                return Collections.emptyList();
            }
            return Collections.singletonList(new UselessParenthesesFix(member.getExpression().getText()));
        }
        return Collections.emptyList();
    }

}
