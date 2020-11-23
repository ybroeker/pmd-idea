package com.github.ybroeker.pmdidea.quickfix;

import com.github.ybroeker.pmdidea.inspection.PsiFiles;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.psi.*;
import com.intellij.testFramework.LightJavaCodeInsightTestCase;
import org.junit.Assert;

import static com.github.ybroeker.pmdidea.ResourceUtil.getInputStreamAsString;

public class UseVarargsFixTest extends LightJavaCodeInsightTestCase {

    public void testUseVarargsFix() throws Exception {
        String fileContent = getInputStreamAsString(UseVarargsFixTest.class.getResourceAsStream("UseVarargsFixTest/testUseVarargsFix/GivenClass.java"));
        final PsiFile fileFromText = PsiFileFactory.getInstance(getProject()).createFileFromText("TestClass.java", JavaFileType.INSTANCE, fileContent);

        final PsiElement element = PsiFiles.getElement(fileFromText, 2, 24);

        final PsiParameter parameter = PsiElements.findParentPsiElement(PsiParameter.class, element);
        UseVarargsFix fix = new UseVarargsFix("");
        fix.applyFix(getProject(), parameter);

        String actual = fileFromText.getText();

        String expected = getInputStreamAsString(UseVarargsFixTest.class.getResourceAsStream("UseVarargsFixTest/testUseVarargsFix/ExpectedClass.java"));

        Assert.assertEquals(expected, actual);
    }

}
