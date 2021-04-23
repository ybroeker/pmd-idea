package com.github.ybroeker.pmdidea.quickfix;

import com.github.ybroeker.pmdidea.inspection.PsiFiles;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.psi.*;
import com.intellij.testFramework.LightJavaCodeInsightTestCase;
import org.junit.Assert;

import static com.github.ybroeker.pmdidea.ResourceUtil.getInputStreamAsString;


public class UseDiamondOperatorFixTest extends LightJavaCodeInsightTestCase {

    String fileContent = getInputStreamAsString(MakeLocalVariableFinalFixTest.class.getResourceAsStream("UseDiamondOperatorFixTest/testUseDiamondOperator/GivenClass.java"));
    String expected = getInputStreamAsString(MakeParameterFinalFixTest.class.getResourceAsStream("UseDiamondOperatorFixTest/testUseDiamondOperator/ExpectedClass.java"));

    public void testUseDiamondOperator() throws Exception {
        final PsiFile fileFromText = PsiFileFactory.getInstance(getProject()).createFileFromText("TestClass.java", JavaFileType.INSTANCE, fileContent);

        final PsiElement element = PsiFiles.getElement(fileFromText, 2, 25);
        final PsiExpression expression = PsiElements.findParentPsiElement(PsiExpression.class, element);

        new UseDiamondOperatorFix().applyFix(getProject(), expression);

        String actual = fileFromText.getText();

        Assert.assertEquals(expected, actual);
    }

}
