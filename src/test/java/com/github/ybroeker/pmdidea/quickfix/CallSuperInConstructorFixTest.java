package com.github.ybroeker.pmdidea.quickfix;

import com.github.ybroeker.pmdidea.inspection.PsiFiles;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.psi.*;
import com.intellij.testFramework.LightJavaCodeInsightTestCase;
import org.junit.Assert;

import static com.github.ybroeker.pmdidea.ResourceUtil.getInputStreamAsString;


public class CallSuperInConstructorFixTest extends LightJavaCodeInsightTestCase {
    String fileContent = getInputStreamAsString(MakeLocalVariableFinalFixTest.class.getResourceAsStream("CallSuperInConstructorFixTest/testCallSuperInConstructor/GivenClass.java"));
    String expected = getInputStreamAsString(MakeParameterFinalFixTest.class.getResourceAsStream("CallSuperInConstructorFixTest/testCallSuperInConstructor/ExpectedClass.java"));

    public void testCallSuperInConstructor() throws Exception {

        final CallSuperInConstructorFix callSuperInConstructorFix = new CallSuperInConstructorFix();

        final PsiFile fileFromText = PsiFileFactory.getInstance(getProject()).createFileFromText("TestClass.java", JavaFileType.INSTANCE, fileContent);
        final PsiElement element = PsiFiles.getElement(fileFromText, 3, 12);
        final PsiMethod target = PsiElements.findParentPsiElement(PsiMethod.class, element);
        callSuperInConstructorFix.applyFix(getProject(), target);

        String actual = fileFromText.getText();

        Assert.assertEquals(expected,actual);
    }

}
