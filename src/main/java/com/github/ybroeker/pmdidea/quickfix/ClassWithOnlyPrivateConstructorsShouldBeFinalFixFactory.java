package com.github.ybroeker.pmdidea.quickfix;

import java.util.Collections;
import java.util.List;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import static com.github.ybroeker.pmdidea.quickfix.PsiElements.findParentPsiElement;

public class ClassWithOnlyPrivateConstructorsShouldBeFinalFixFactory implements QuickFixFactory {

    @Override
    public @NotNull List<LocalQuickFix> getQuickFix(@NotNull final PsiElement target, @NotNull final PmdRuleViolation violation) {
        if (!violation.getPmdRule().getName().equals("ClassWithOnlyPrivateConstructorsShouldBeFinal")) {
            return Collections.emptyList();
        }

        final PsiClass clazz = findParentPsiElement(PsiClass.class, target);
        if (clazz == null) {
            return Collections.emptyList();
        }

        return Collections.singletonList(new ClassWithOnlyPrivateConstructorsShouldBeFinalFix());
    }

}
