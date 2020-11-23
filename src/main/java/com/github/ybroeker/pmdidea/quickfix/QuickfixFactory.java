package com.github.ybroeker.pmdidea.quickfix;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.openapi.components.Service;
import com.intellij.psi.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public final class QuickfixFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuickfixFactory.class);


    public LocalQuickFix getQuickFix(final PsiElement target, final PmdRuleViolation violation) {
        LOGGER.trace("Try create QuickFix for rule '{}' and element '{}'", violation.getPmdRule().getName(), target);

        if (violation.getPmdRule().getName().equals("MethodArgumentCouldBeFinal")) {
            PsiParameter parameter = findParameter(target);
            if (parameter == null) {
                return null;
            }
            return new MakeParameterFinalFix(parameter.getName());
        }
        if (violation.getPmdRule().getName().equals("LocalVariableCouldBeFinal")) {
            PsiVariable variable = findPsiElement(PsiVariable.class, target);
            if (variable == null) {
                return null;
            }
            return new MakeLocalVariableFinalFix(variable.getName());
        }

        if (violation.getPmdRule().getName().equals("CommentDefaultAccessModifier")) {
            PsiMember member = findPsiElement(PsiMember.class, target);
            if (member == null) {
                return null;
            }
            return new CommentDefaultAccessModifierFix(member.getName());
        }
        if (violation.getPmdRule().getName().equals("UselessParentheses")) {
            PsiParenthesizedExpression member = findPsiElement(PsiParenthesizedExpression.class, target);
            if (member == null) {
                return null;
            }
            if (member.getExpression() == null) {
                return null;
            }
            return new UselessParenthesesFix(member.getExpression().getText());
        }

        return null;

    }

    public static <T extends PsiElement> T findPsiElement(Class<? extends T> clazz, PsiElement target) {
        PsiElement element = target;
        while (element != null && !(clazz.isInstance(element))) {
            element = element.getParent();
        }
        return clazz.cast(element);
    }

    public static PsiParameter findParameter(PsiElement target) {
        return findPsiElement(PsiParameter.class, target);
    }

}
