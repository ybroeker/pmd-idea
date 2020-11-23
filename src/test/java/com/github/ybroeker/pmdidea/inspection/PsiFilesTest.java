package com.github.ybroeker.pmdidea.inspection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.psi.*;
import com.intellij.testFramework.LightJavaCodeInsightTestCase;


public class PsiFilesTest extends LightJavaCodeInsightTestCase {

    //Uses short parameter name to detect off-by-one errors
    public void testShouldReturnCorrectPisElement() throws Exception {
        String fileContent = new BufferedReader(new InputStreamReader(PsiFilesTest.class.getResourceAsStream("/TestClass.java"))).lines().collect(Collectors.joining("\n"));
        final PsiFile fileFromText = PsiFileFactory.getInstance(getProject()).createFileFromText("TestClass.java", JavaFileType.INSTANCE, fileContent);

        final PsiElement element = PsiFiles.getElement(fileFromText, 6, 42);

        assertInstanceOf(element, PsiIdentifier.class);
        assertEquals("s", element.getText());
    }

}
