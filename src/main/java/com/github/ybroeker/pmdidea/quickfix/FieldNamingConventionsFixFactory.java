package com.github.ybroeker.pmdidea.quickfix;

import java.util.Collections;
import java.util.List;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import org.jetbrains.annotations.NotNull;

import static com.github.ybroeker.pmdidea.quickfix.PsiElements.findParentPsiElement;

public class FieldNamingConventionsFixFactory implements QuickFixFactory {

    @Override
    public @NotNull List<LocalQuickFix> getQuickFix(@NotNull final PsiElement target, @NotNull final PmdRuleViolation violation) {
        if (!violation.getPmdRule().getName().equals("FieldNamingConventions")) {
            return Collections.emptyList();
        }

        final PsiIdentifier member = findParentPsiElement(PsiIdentifier.class, target);
        if (member == null) {
            return Collections.emptyList();
        }

        //TODO:
        return Collections.singletonList(new RenameToFix(member.getText()));
    }

}
