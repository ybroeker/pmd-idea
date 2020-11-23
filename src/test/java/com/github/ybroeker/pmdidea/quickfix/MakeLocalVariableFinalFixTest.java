package com.github.ybroeker.pmdidea.quickfix;

import com.github.ybroeker.pmdidea.inspection.PsiElements;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.psi.*;
import com.intellij.testFramework.LightJavaCodeInsightTestCase;
import org.junit.Assert;

import static com.github.ybroeker.pmdidea.ResourceUtil.getInputStreamAsString;

public class MakeLocalVariableFinalFixTest extends LightJavaCodeInsightTestCase {

    public void testMakeLocalVariableFinal() throws Exception {
        String fileContent = getInputStreamAsString(MakeLocalVariableFinalFixTest.class.getResourceAsStream("MakeLocalVariableFinalFixTest/testMakeLocalVariableFinal/GivenClass.java"));
        final PsiFile fileFromText = PsiFileFactory.getInstance(getProject()).createFileFromText("TestClass.java", JavaFileType.INSTANCE, fileContent);

        final PsiElement element = PsiElements.getElement(fileFromText, 3, 9);

        final PsiVariable target = QuickfixFactory.findPsiElement(PsiVariable.class, element);
        MakeLocalVariableFinalFix quickFix = new MakeLocalVariableFinalFix("String s");
        quickFix.applyFix(getProject(), target);

        String actual = fileFromText.getText();

        String expected = getInputStreamAsString(MakeParameterFinalFixTest.class.getResourceAsStream("MakeLocalVariableFinalFixTest/testMakeLocalVariableFinal/ExpectedClass.java"));

        Assert.assertEquals(expected,actual);
    }

}
