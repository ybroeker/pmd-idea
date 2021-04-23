package com.github.ybroeker.pmdidea.quickfix;

import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

import com.intellij.codeInspection.RemoveRedundantTypeArgumentsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class UseDiamondOperatorFix extends AbstractLocalQuickFix<PsiExpression> {

    @Override
    protected void applyFix(final @NotNull Project project, final @NotNull PsiExpression target) {
        final PsiReferenceParameterList psiReferenceParameterList = findPsiReferenceParameterList(target);
        if (psiReferenceParameterList == null) {
            return;
        }
        RemoveRedundantTypeArgumentsUtil.replaceExplicitWithDiamond(psiReferenceParameterList);
    }


    private PsiReferenceParameterList findPsiReferenceParameterList(final @NotNull PsiElement target) {
        final PsiExpression expression = PsiElements.findParentPsiElement(PsiExpression.class, target);

        final FindChildElementVisitor<PsiReferenceParameterList> findChildElementVisitor = new FindChildElementVisitor<>(PsiReferenceParameterList.class);
        expression.acceptChildren(findChildElementVisitor);

        for (final PsiReferenceParameterList psiReferenceParameterList : findChildElementVisitor.elements) {
            if (psiReferenceParameterList.getTypeArguments().length > 0) {
                return psiReferenceParameterList;
            }
        }
        return null;

    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getFamilyName() {
        return "Use diamond operator";
    }
}
