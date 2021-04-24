package com.github.ybroeker.pmdidea.quickfix;

import com.github.ybroeker.pmdidea.inspection.PsiFiles;
import com.github.ybroeker.pmdidea.test.*;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.PsiElementFactoryImpl;
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@IdeaProjectTest
@ExtendWith(ResourceResolver.class)
public class CollapsibleIfStatementsFixTest {

    @Test
    void testCollapsibleIfStatement(final IdeaProjectTestFixture fixture,
                                    @Resource("Given.java") final String given,
                                    @Resource("Expected.java") final String expected ) {
        final PsiFile fileFromText = PsiFileFactory.getInstance(fixture.getProject()).createFileFromText("TestClass.java", JavaFileType.INSTANCE, given);

        final PsiElement element = PsiFiles.getElement(fileFromText, 7, 13);
        final PsiIfStatement innerIf = PsiElements.findParentPsiElement(PsiIfStatement.class, element);

        new CollapsibleIfStatementsFix().applyFix(fixture.getProject(), innerIf);

        String actual = fileFromText.getText();

        Assertions.assertEquals(expected,actual);
    }
}
