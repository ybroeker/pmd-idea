package com.github.ybroeker.pmdidea.quickfix;

import com.github.ybroeker.pmdidea.inspection.PsiElements;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.psi.*;
import com.intellij.testFramework.LightJavaCodeInsightTestCase;
import org.junit.Assert;

import static com.github.ybroeker.pmdidea.ResourceUtil.getInputStreamAsString;


public class UselessParenthesesFixTest  extends LightJavaCodeInsightTestCase {

    public void testRemoveUselessParentheses() throws Exception {
        String fileContent = getInputStreamAsString(UselessParenthesesFixTest.class.getResourceAsStream("UselessParenthesesFixTest/testRemoveUselessParentheses/GivenClass.java"));
        final PsiFile fileFromText = PsiFileFactory.getInstance(getProject()).createFileFromText("TestClass.java", JavaFileType.INSTANCE, fileContent);

        final PsiElement element = PsiElements.getElement(fileFromText, 3, 18);

        final PsiParenthesizedExpression expression = QuickfixFactory.findPsiElement(PsiParenthesizedExpression.class, element);
        UselessParenthesesFix makeParameterFinalFix = new UselessParenthesesFix("(1 + 1)");
        makeParameterFinalFix.applyFix(expression);

        String actual = fileFromText.getText();

        String expected = getInputStreamAsString(UselessParenthesesFixTest.class.getResourceAsStream("UselessParenthesesFixTest/testRemoveUselessParentheses/ExpectedClass.java"));

        Assert.assertEquals(expected,actual);
    }

}
