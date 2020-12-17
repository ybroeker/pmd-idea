package com.github.ybroeker.pmdidea.inspection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.psi.*;
import com.intellij.testFramework.LightJavaCodeInsightTestCase;
import org.junit.Assert;


public class PsiFilesTest extends LightJavaCodeInsightTestCase {

    //Uses short parameter name to detect off-by-one errors
    public void testShouldReturnCorrectPisElement() throws Exception {
        String fileContent = new BufferedReader(new InputStreamReader(PsiFilesTest.class.getResourceAsStream("/TestClass.java"))).lines().collect(Collectors.joining("\n"));
        final PsiFile fileFromText = PsiFileFactory.getInstance(getProject()).createFileFromText("TestClass.java", JavaFileType.INSTANCE, fileContent);

        final PsiElement element = PsiFiles.getElement(fileFromText, 6, 42);

        assertInstanceOf(element, PsiIdentifier.class);
        assertEquals("s", element.getText());
    }

    public void testLineOutOfBoundsShouldReturnNull() {
        String fileContent = new BufferedReader(new InputStreamReader(PsiFilesTest.class.getResourceAsStream("/TestClass.java"))).lines().collect(Collectors.joining("\n"));
        final PsiFile fileFromText = PsiFileFactory.getInstance(getProject()).createFileFromText("TestClass.java", JavaFileType.INSTANCE, fileContent);

        final PsiElement element = PsiFiles.getElement(fileFromText, 11, 2);

        Assert.assertNull(element);
    }

    public void testOffsetOutOfBoundsShouldReturnNull() {
        String fileContent = new BufferedReader(new InputStreamReader(PsiFilesTest.class.getResourceAsStream("/TestClass.java"))).lines().collect(Collectors.joining("\n"));
        final PsiFile fileFromText = PsiFileFactory.getInstance(getProject()).createFileFromText("TestClass.java", JavaFileType.INSTANCE, fileContent);

        final PsiElement element = PsiFiles.getElement(fileFromText, 9, 200);

        Assert.assertNull(element);
    }

    public void testNegativeLineShouldReturnNull() {
        String fileContent = new BufferedReader(new InputStreamReader(PsiFilesTest.class.getResourceAsStream("/TestClass.java"))).lines().collect(Collectors.joining("\n"));
        final PsiFile fileFromText = PsiFileFactory.getInstance(getProject()).createFileFromText("TestClass.java", JavaFileType.INSTANCE, fileContent);

        final PsiElement element = PsiFiles.getElement(fileFromText, -1, 0);

        Assert.assertNull(element);
    }

    public void testNegativeOffsetShouldReturnNull() {
        String fileContent = new BufferedReader(new InputStreamReader(PsiFilesTest.class.getResourceAsStream("/TestClass.java"))).lines().collect(Collectors.joining("\n"));
        final PsiFile fileFromText = PsiFileFactory.getInstance(getProject()).createFileFromText("TestClass.java", JavaFileType.INSTANCE, fileContent);

        final PsiElement element = PsiFiles.getElement(fileFromText, 1, -200);

        Assert.assertNull(element);
    }

}
