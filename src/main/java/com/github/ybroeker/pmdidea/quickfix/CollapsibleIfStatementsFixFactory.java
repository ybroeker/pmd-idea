package com.github.ybroeker.pmdidea.quickfix;

import java.util.Collections;
import java.util.List;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import static com.github.ybroeker.pmdidea.quickfix.PsiElements.findParentPsiElement;

public class CollapsibleIfStatementsFixFactory implements QuickFixFactory {

    @Override
    public @NotNull List<LocalQuickFix> getQuickFix(@NotNull final PsiElement target, @NotNull final PmdRuleViolation violation) {
        if (!violation.getPmdRule().getName().equals("CollapsibleIfStatements")) {
            return Collections.emptyList();
        }

        final PsiIfStatement ifStatement = findParentPsiElement(PsiIfStatement.class, target);
        if (ifStatement == null) {
            return Collections.emptyList();
        }

        return Collections.singletonList(new CollapsibleIfStatementsFix());
    }

}
