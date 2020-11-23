package com.github.ybroeker.pmdidea.quickfix;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.PsiElementFactoryImpl;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class CommentDefaultAccessModifierFix implements LocalQuickFix {

    private final String name;

    /* default */ CommentDefaultAccessModifierFix(final String name) {
        this.name = name;
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getName() {
        return "Comment default access modifier for member '" + name + "'";

    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getFamilyName() {
        return "Comment default access modifier";
    }

    @Override
    public void applyFix(@NotNull final Project project, @NotNull final ProblemDescriptor descriptor) {
        final PsiMember member = QuickfixFactory.findPsiElement(PsiMember.class, descriptor.getEndElement());
        if (member == null) {
            return;
        }
        final PsiJavaParserFacade psiJavaParserFacade = new PsiElementFactoryImpl(project);
        final PsiComment commentFromText = psiJavaParserFacade.createCommentFromText("/* default */", null);

        member.getParent().addBefore(commentFromText, member);
    }

}
