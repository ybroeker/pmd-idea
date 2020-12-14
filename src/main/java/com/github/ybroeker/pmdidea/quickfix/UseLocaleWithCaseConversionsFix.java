package com.github.ybroeker.pmdidea.quickfix;

import java.util.*;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.impl.PsiJavaParserFacadeImpl;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class UseLocaleWithCaseConversionsFix extends AbstractLocalQuickFix<PsiMethodCallExpression> {

    private static final List<String> METHOD_NAMES = Collections.unmodifiableList(Arrays.asList("toLowerCase", "toUpperCase"));

    private final LocaleToUse localeToUse;

    public enum LocaleToUse {
        ROOT("Locale.ROOT", "java.util.Locale.ROOT"), DEFAULT("Locale.getDefault()", "java.util.Locale.getDefault()");

        public final String shortName;
        public final String fullyQualified;

        LocaleToUse(final String shortName, final String fullyQualified) {
            this.shortName = shortName;
            this.fullyQualified = fullyQualified;
        }
    }

    public UseLocaleWithCaseConversionsFix(@NotNull final LocaleToUse localeToUse) {
        super();
        this.localeToUse = Objects.requireNonNull(localeToUse);
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getName() {
        return "Use '" + localeToUse.shortName + "'";
    }

    @NotNull
    @Override
    public String getFamilyName() {
        return "Use explicit locale";
    }

    @Override
    protected void applyFix(@NotNull final Project project, @NotNull final PsiMethodCallExpression methodCall) {
        final PsiReferenceExpression referenceExpression = methodCall.getMethodExpression();
        final PsiIdentifier methodName = (PsiIdentifier) referenceExpression.getLastChild();

        if (!METHOD_NAMES.contains(methodName.getText())) {
            return;
        }

        final PsiJavaParserFacade psiJavaParserFacade = new PsiJavaParserFacadeImpl(project);
        final PsiExpression rootLocale = psiJavaParserFacade.createExpressionFromText(localeToUse.fullyQualified, referenceExpression.getContainingFile());

        final PsiElement added = methodCall.getArgumentList().add(rootLocale);
        final TextRange textRange = added.getTextRange();

        final JavaCodeStyleManager instance = JavaCodeStyleManager.getInstance(project);
        instance.shortenClassReferences(referenceExpression.getContainingFile(), textRange.getStartOffset(), textRange.getEndOffset());
    }

}
