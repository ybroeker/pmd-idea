package com.github.ybroeker.pmdidea.quickfix;

import java.lang.reflect.ParameterizedType;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractLocalQuickFix<T extends PsiElement> implements LocalQuickFix {

    private final Class<T> targetElementType;

    public AbstractLocalQuickFix() {
        final ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        if (!(pt.getActualTypeArguments()[0] instanceof Class)) {
            throw new AssertionError("Actual type argument is not a class");
        }
        this.targetElementType = (Class<T>) pt.getActualTypeArguments()[0];
    }

    public final void applyFix(@NotNull final Project project, @NotNull final ProblemDescriptor descriptor) {
        final T parameter = QuickfixFactory.findPsiElement(targetElementType, descriptor.getPsiElement());
        if (parameter == null) {
            return;
        }
        applyFix(project, parameter);
    }

    protected abstract void applyFix(@NotNull final Project project, @NotNull final T target);
}
