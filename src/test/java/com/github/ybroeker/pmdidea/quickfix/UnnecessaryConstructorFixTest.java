package com.github.ybroeker.pmdidea.quickfix;

import java.io.IOException;

import com.github.ybroeker.pmdidea.inspection.PsiFiles;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.psi.*;
import com.intellij.testFramework.LightJavaCodeInsightTestCase;
import org.junit.Assert;

import static com.github.ybroeker.pmdidea.ResourceUtil.getInputStreamAsString;

public class UnnecessaryConstructorFixTest extends LightJavaCodeInsightTestCase {

    public void testUnnecessaryConstructorFix() throws IOException {
        String fileContent = getInputStreamAsString(UnnecessaryConstructorFixTest.class.getResourceAsStream("UnnecessaryConstructorFixTest/testUnnecessaryConstructorFix/GivenClass.java"));
        final PsiFile fileFromText = PsiFileFactory.getInstance(getProject()).createFileFromText("TestClass.java", JavaFileType.INSTANCE, fileContent);

        final PsiElement element = PsiFiles.getElement(fileFromText, 2, 5);

        final PsiMethod target = PsiElements.findParentPsiElement(PsiMethod.class, element);
        UnnecessaryConstructorFix quickFix = new UnnecessaryConstructorFix();
        quickFix.applyFix(getProject(), target);

        String actual = fileFromText.getText();

        String expected = getInputStreamAsString(UnnecessaryConstructorFixTest.class.getResourceAsStream("UnnecessaryConstructorFixTest/testUnnecessaryConstructorFix/ExpectedClass.java"));

        Assert.assertEquals(expected,actual);

    }

}
