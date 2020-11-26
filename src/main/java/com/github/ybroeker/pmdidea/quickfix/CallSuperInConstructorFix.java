package com.github.ybroeker.pmdidea.quickfix;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.PsiElementFactoryImpl;
import org.jetbrains.annotations.NotNull;


public class CallSuperInConstructorFix extends AbstractLocalQuickFix<PsiMethod> {

    @Override
    protected void applyFix(final @NotNull Project project, final @NotNull PsiMethod target) {
        final PsiJavaParserFacade psiJavaParserFacade = new PsiElementFactoryImpl(project);

        final PsiCodeBlock codeBlock = getCodeBlock(target);
        if (codeBlock == null) {
            return;
        }

        final PsiStatement superCall = psiJavaParserFacade.createStatementFromText("super();", target.getContainingFile());
        codeBlock.addAfter(superCall, codeBlock.getFirstChild());
        CodeStyleManager.getInstance(project).reformat(codeBlock);
    }

    private PsiCodeBlock getCodeBlock(final @NotNull PsiMethod target) {
        for (final PsiElement child : target.getChildren()) {
            if (child instanceof PsiCodeBlock) {
                return (PsiCodeBlock) child;
            }
        }
        return null;
    }

    @Override
    public @NotNull String getFamilyName() {
        return "Add super();";
    }
}
