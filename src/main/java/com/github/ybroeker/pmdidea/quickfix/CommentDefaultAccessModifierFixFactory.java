package com.github.ybroeker.pmdidea.quickfix;

import java.util.*;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMember;
import org.jetbrains.annotations.NotNull;

import static com.github.ybroeker.pmdidea.quickfix.PsiElements.findParentPsiElement;

public class CommentDefaultAccessModifierFixFactory implements QuickFixFactory {

    @Override
    public @NotNull List<LocalQuickFix> getQuickFix(@NotNull final PsiElement target, @NotNull final PmdRuleViolation violation) {
        if (violation.getPmdRule().getName().equals("CommentDefaultAccessModifier")) {
            PsiMember member = findParentPsiElement(PsiMember.class, target);
            if (member == null) {
                return Collections.emptyList();
            }
            return Arrays.asList(new CommentDefaultAccessModifierFix(member.getName(), "default"),
                    new CommentDefaultAccessModifierFix(member.getName(), "package"));
        }
        return Collections.emptyList();
    }
}
