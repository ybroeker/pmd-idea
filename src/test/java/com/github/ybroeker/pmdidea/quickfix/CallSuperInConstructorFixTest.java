package com.github.ybroeker.pmdidea.quickfix;

import java.util.Arrays;

import com.github.ybroeker.pmdidea.inspection.PsiFiles;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.PsiElementFactoryImpl;
import com.intellij.testFramework.LightJavaCodeInsightTestCase;
import org.junit.Assert;

import static com.github.ybroeker.pmdidea.ResourceUtil.getInputStreamAsString;

public class CallSuperInConstructorFixTest extends LightJavaCodeInsightTestCase {
    String fileContent = getInputStreamAsString(MakeLocalVariableFinalFixTest.class.getResourceAsStream("CallSuperInConstructorFixTest/testCallSuperInConstructor/GivenClass.java"));
    String expected = getInputStreamAsString(MakeParameterFinalFixTest.class.getResourceAsStream("CallSuperInConstructorFixTest/testCallSuperInConstructor/ExpectedClass.java"));

    public void testCallSuperInConstructor() throws Exception {
        final PsiFile fileFromText = PsiFileFactory.getInstance(getProject()).createFileFromText("TestClass.java", JavaFileType.INSTANCE, fileContent);

        final PsiElement element = PsiFiles.getElement(fileFromText, 3, 12);
        final PsiJavaParserFacade psiJavaParserFacade = new PsiElementFactoryImpl(getProject());

        final PsiMethod target = PsiElements.findParentPsiElement(PsiMethod.class, element);
        System.out.println(target);
        System.out.println("target.isConstructor() = " + target.isConstructor());
        System.out.println("target.getChildren() = " + Arrays.toString(target.getChildren()));
        for (final PsiElement child : target.getChildren()) {
            if (child instanceof PsiCodeBlock) {
                final PsiStatement superCall = psiJavaParserFacade.createStatementFromText("super();", fileFromText);
                child.addAfter(superCall, child.getFirstChild());
                CodeStyleManager.getInstance(getProject()).reformat(child);
                break;
            }
        }

        //final MakeLocalVariableFinalFix quickFix = new MakeLocalVariableFinalFix("String s");
        //quickFix.applyFix(getProject(), target);

        final String actual = fileFromText.getText();


        Assert.assertEquals(expected, actual);
    }

}
