package com.github.ybroeker.pmdidea.quickfix;

import com.intellij.psi.PsiElement;


public class PsiElements {

    public static <T extends PsiElement> T findParentPsiElement(final Class<? extends T> clazz, final PsiElement target) {
        PsiElement element = target;
        while (element != null && !(clazz.isInstance(element))) {
            element = element.getParent();
        }
        return clazz.cast(element);
    }

}
