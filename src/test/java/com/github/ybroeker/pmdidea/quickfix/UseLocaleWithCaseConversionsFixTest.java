package com.github.ybroeker.pmdidea.quickfix;

import com.github.ybroeker.pmdidea.inspection.PsiFiles;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.psi.*;
import com.intellij.testFramework.*;
import org.assertj.core.api.Assertions;

import static com.github.ybroeker.pmdidea.ResourceUtil.getInputStreamAsString;


public class UseLocaleWithCaseConversionsFixTest  extends LightJavaCodeInsightTestCase {


    public void testUseRootLocale() throws Exception {
        String fileContent = getInputStreamAsString(UselessParenthesesFixTest.class.getResourceAsStream("UseLocaleWithCaseConversionsFixTest/GivenClass.java"));
        final PsiFile fileFromText = PsiFileFactory.getInstance(getProject()).createFileFromText("TestClass.java", JavaFileType.INSTANCE, fileContent);

        final PsiElement element = PsiFiles.getElement(fileFromText, 3, 9);

        final UseLocaleWithCaseConversionsFix useLocaleWithCaseConversionsFix = new UseLocaleWithCaseConversionsFix(UseLocaleWithCaseConversionsFix.LocaleToUse.ROOT);

        final PsiMethodCallExpression reference = PsiElements.findParentPsiElement(PsiMethodCallExpression.class, element);
        useLocaleWithCaseConversionsFix.applyFix(getProject(),reference);

        PsiTestUtil.checkFileStructure(fileFromText);

        String expected = getInputStreamAsString(UselessParenthesesFixTest.class.getResourceAsStream("UseLocaleWithCaseConversionsFixTest/testUseRootLocale/ExpectedClass.java"));

        Assertions.assertThat(fileFromText.getText()).isEqualTo(expected);
    }

    public void testUseDefaultLocale() throws Exception {
        String fileContent = getInputStreamAsString(UselessParenthesesFixTest.class.getResourceAsStream("UseLocaleWithCaseConversionsFixTest/GivenClass.java"));
        final PsiFile fileFromText = PsiFileFactory.getInstance(getProject()).createFileFromText("TestClass.java", JavaFileType.INSTANCE, fileContent);

        final PsiElement element = PsiFiles.getElement(fileFromText, 3, 9);

        final UseLocaleWithCaseConversionsFix useLocaleWithCaseConversionsFix = new UseLocaleWithCaseConversionsFix(UseLocaleWithCaseConversionsFix.LocaleToUse.DEFAULT);

        final PsiMethodCallExpression reference = PsiElements.findParentPsiElement(PsiMethodCallExpression.class, element);
        useLocaleWithCaseConversionsFix.applyFix(getProject(),reference);

        PsiTestUtil.checkFileStructure(fileFromText);

        String expected = getInputStreamAsString(UselessParenthesesFixTest.class.getResourceAsStream("UseLocaleWithCaseConversionsFixTest/testUseDefaultLocale/ExpectedClass.java"));

        Assertions.assertThat(fileFromText.getText()).isEqualTo(expected);
    }






}
