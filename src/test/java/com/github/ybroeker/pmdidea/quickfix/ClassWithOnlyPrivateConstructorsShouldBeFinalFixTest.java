package com.github.ybroeker.pmdidea.quickfix;

import com.github.ybroeker.pmdidea.inspection.PsiFiles;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.psi.*;
import com.intellij.testFramework.LightJavaCodeInsightTestCase;
import org.junit.Assert;

import static com.github.ybroeker.pmdidea.ResourceUtil.getInputStreamAsString;


public class ClassWithOnlyPrivateConstructorsShouldBeFinalFixTest extends LightJavaCodeInsightTestCase {
    String fileContent = getInputStreamAsString(MakeLocalVariableFinalFixTest.class.getResourceAsStream("ClassWithOnlyPrivateConstructorsShouldBeFinalFixTest/testClassWithOnlyPrivateConstructorsShouldBeFinal/GivenClass.java"));
    String expected = getInputStreamAsString(MakeParameterFinalFixTest.class.getResourceAsStream("ClassWithOnlyPrivateConstructorsShouldBeFinalFixTest/testClassWithOnlyPrivateConstructorsShouldBeFinal/ExpectedClass.java"));

    public void testClassWithOnlyPrivateConstructorsShouldBeFinal() throws Exception {
        final PsiFile fileFromText = PsiFileFactory.getInstance(getProject()).createFileFromText("TestClass.java", JavaFileType.INSTANCE, fileContent);
        final PsiElement element = PsiFiles.getElement(fileFromText, 1, 11);
        final PsiClass target = PsiElements.findParentPsiElement(PsiClass.class, element);

        final ClassWithOnlyPrivateConstructorsShouldBeFinalFix quickFix = new ClassWithOnlyPrivateConstructorsShouldBeFinalFix();

        quickFix.applyFix(getProject(), target);

        String actual = fileFromText.getText();

        Assert.assertEquals(expected,actual);
    }

}
