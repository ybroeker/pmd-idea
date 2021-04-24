package com.github.ybroeker.pmdidea.quickfix;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.PsiElementFactoryImpl;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;


public class CollapsibleIfStatementsFix extends AbstractLocalQuickFix<PsiIfStatement> {

    @Override
    protected void applyFix(final @NotNull Project project, final @NotNull PsiIfStatement innerIf) {
        final PsiIfStatement outerIf = PsiElements.findParentPsiElement(PsiIfStatement.class, innerIf.getParent());
        final PsiStatement body = innerIf.getThenBranch();
        if (body == null || outerIf.getCondition() == null || innerIf.getCondition() == null) {
            return;
        }

        final PsiJavaParserFacade psiJavaParserFacade = new PsiElementFactoryImpl(project);
        final PsiExpression mergedCondition = psiJavaParserFacade.createExpressionFromText(
                outerIf.getCondition().getText() + " && " + innerIf.getCondition().getText(),
                innerIf.getContainingFile());

        outerIf.getCondition().replace(mergedCondition);
        outerIf.setThenBranch(body);
        CodeStyleManager.getInstance(project).reformat(outerIf);
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getFamilyName() {
        return "Merge if statements";
    }
}
