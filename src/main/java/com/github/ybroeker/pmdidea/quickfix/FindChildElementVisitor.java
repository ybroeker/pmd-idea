package com.github.ybroeker.pmdidea.quickfix;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;

class FindChildElementVisitor<T extends PsiElement> extends PsiElementVisitor {
    private final Class<T> clazz;

    public FindChildElementVisitor(final Class<T> clazz) {
        this.clazz = clazz;
    }

    List<T> elements = new ArrayList<>();

    @Override
    public void visitElement(final PsiElement element) {
        if (clazz.isAssignableFrom(element.getClass())) {
            elements.add((T) element);
        }
        element.acceptChildren(this);
    }
}
