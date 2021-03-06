package com.github.ybroeker.pmdidea.quickfix;

import com.github.ybroeker.pmdidea.inspection.PsiFiles;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.psi.*;
import com.intellij.testFramework.LightJavaCodeInsightTestCase;
import org.junit.Assert;

import static com.github.ybroeker.pmdidea.ResourceUtil.getInputStreamAsString;


public class MakeParameterFinalFixTest extends LightJavaCodeInsightTestCase {

    public void testMakeParameterFinal() throws Exception {
        String fileContent = getInputStreamAsString(MakeParameterFinalFixTest.class.getResourceAsStream("MakeParameterFinalFixTest/testMakeParameterFinal/GivenClass.java"));
        final PsiFile fileFromText = PsiFileFactory.getInstance(getProject()).createFileFromText("TestClass.java", JavaFileType.INSTANCE, fileContent);

        final PsiElement element = PsiFiles.getElement(fileFromText, 2, 24);

        final PsiParameter parameter = PsiElements.findParentPsiElement(PsiParameter.class, element);
        MakeParameterFinalFix makeParameterFinalFix = new MakeParameterFinalFix("test");
        makeParameterFinalFix.applyFix(getProject(), parameter);

        String actual = fileFromText.getText();

        String expected = getInputStreamAsString(MakeParameterFinalFixTest.class.getResourceAsStream("MakeParameterFinalFixTest/testMakeParameterFinal/ExpectedClass.java"));

        Assert.assertEquals(expected,actual);
    }

}
