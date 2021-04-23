package com.github.ybroeker.pmdidea.quickfix;

import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

import com.github.ybroeker.pmdidea.inspection.PsiFiles;
import com.intellij.codeInsight.intention.AddAnnotationPsiFix;
import com.intellij.codeInspection.*;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.impl.source.codeStyle.JavaCodeStyleManagerImpl;
import com.intellij.testFramework.LightJavaCodeInsightTestCase;
import com.intellij.testFramework.MockProblemDescriptor;
import com.intellij.util.ThreeState;
import org.junit.Assert;

import static com.github.ybroeker.pmdidea.ResourceUtil.getInputStreamAsString;

public class MissingOverrideFixTest extends LightJavaCodeInsightTestCase {

    String fileContent = getInputStreamAsString(MakeLocalVariableFinalFixTest.class.getResourceAsStream("MissingOverrideFixTest/testMissingOverride/GivenClass.java"));
    String expected = getInputStreamAsString(MakeParameterFinalFixTest.class.getResourceAsStream("MissingOverrideFixTest/testMissingOverride/ExpectedClass.java"));

    public void testMissingOverride() throws Exception {
        final PsiFile fileFromText = PsiFileFactory.getInstance(getProject()).createFileFromText("TestClass.java", JavaFileType.INSTANCE, fileContent);
        final PsiMethod target = PsiElements.findParentPsiElement(PsiMethod.class, PsiFiles.getElement(fileFromText, 2, 12));

        new MissingOverrideFix().applyFix(getProject(), target);

        String actual = fileFromText.getText();

        Assert.assertEquals(expected, actual);
    }

}
