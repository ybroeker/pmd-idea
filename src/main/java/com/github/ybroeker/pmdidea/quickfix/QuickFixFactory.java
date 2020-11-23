package com.github.ybroeker.pmdidea.quickfix;

import java.util.List;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public interface QuickFixFactory {

    @NotNull
    List<LocalQuickFix> getQuickFix(@NotNull PsiElement target,@NotNull PmdRuleViolation violation);
}
