package com.github.ybroeker.pmdidea.quickfix;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.PsiElementFactoryImpl;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class CommentDefaultAccessModifierFix extends AbstractLocalQuickFix<PsiMember> {

    private final String name;

    private final String commentContent;

    CommentDefaultAccessModifierFix(final String name, final String commentContent) {
        this.name = name;
        this.commentContent = commentContent;
    }

    public CommentDefaultAccessModifierFix(final String name) {
        this(name, "default");
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getFamilyName() {
        return "Comment default access modifier with '/* " + commentContent + " */'";
    }

    @Override
    protected void applyFix(@NotNull final Project project, @NotNull final PsiMember member) {
        final PsiJavaParserFacade psiJavaParserFacade = new PsiElementFactoryImpl(project);
        final String comment = "/* " + commentContent + " */";
        final PsiComment commentFromText = psiJavaParserFacade.createCommentFromText(comment, null);
        member.getParent().addBefore(commentFromText, member);
    }

}
