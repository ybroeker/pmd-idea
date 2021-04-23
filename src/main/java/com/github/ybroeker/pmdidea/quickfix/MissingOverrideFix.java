package com.github.ybroeker.pmdidea.quickfix;

import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class MissingOverrideFix extends AbstractLocalQuickFix<PsiMethod> {

    @Override
    protected void applyFix(final @NotNull Project project, final @NotNull PsiMethod target) {
        PsiAnnotation added = target.getModifierList().addAnnotation(CommonClassNames.JAVA_LANG_OVERRIDE);
        JavaCodeStyleManager.getInstance(project).shortenClassReferences(added);
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getFamilyName() {
        return "Add @Override";
    }
}
