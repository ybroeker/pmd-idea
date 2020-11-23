package com.github.ybroeker.pmdidea.quickfix;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class UseVarargsFix extends AbstractLocalQuickFix<PsiParameter> {

    private String name;

    public UseVarargsFix(@NotNull final String name) {
        this.name = name;
    }

    @Override
    protected void applyFix(final @NotNull Project project, final @NotNull PsiParameter target) {
        target.normalizeDeclaration();
        final PsiType type = target.getType();
        if (!(type instanceof PsiArrayType)) {
            return;
        }
        final PsiArrayType arrayType = (PsiArrayType) type;
        final PsiType componentType = arrayType.getComponentType();
        final PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);
        final PsiType ellipsisType = new PsiEllipsisType(componentType, TypeAnnotationProvider.Static.create(type.getAnnotations()));
        final PsiTypeElement newTypeElement = factory.createTypeElement(ellipsisType);
        final PsiTypeElement typeElement = target.getTypeElement();
        if (typeElement != null) {
            typeElement.replace(newTypeElement);
        }
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getName() {
        return "Use varargs for parameter '" + name + "'";
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getFamilyName() {
        return "Use varargs";
    }
}
