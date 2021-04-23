package com.github.ybroeker.pmdidea.quickfix;

import java.util.*;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.openapi.components.Service;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public final class DelegateQuickFixFactory implements QuickFixFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelegateQuickFixFactory.class);

    private final List<QuickFixFactory> factorys = Arrays.asList(
            new MakeParameterFinalFixFactory(),
            new MakeLocalVariableFinalFixFactory(),
            new CommentDefaultAccessModifierFixFactory(),
            new UselessParenthesesFixFactory(),
            new UseVarargsFixFactory(),
            new UnnecessaryConstructorFixFactory(),
            new FieldNamingConventionsFixFactory(),
            new MissingSerialVersionUIDFixFactory(),
            new CallSuperInConstructorFixFactory(),
            new UseLocaleWithCaseConversionsFixFactory(),
            new ClassWithOnlyPrivateConstructorsShouldBeFinalFixFactory(),
            new UseDiamondOperatorFixFactory(),
            new SuppressPmdFixFactory()
    );

    @Override
    public @NotNull List<LocalQuickFix> getQuickFix(final @NotNull PsiElement target, final @NotNull PmdRuleViolation violation) {
        LOGGER.trace("Try create QuickFix for rule '{}' and element '{}'", violation.getPmdRule().getName(), target);

        final List<LocalQuickFix> localQuickFixes = new ArrayList<>();
        for (final QuickFixFactory factory : factorys) {
            localQuickFixes.addAll(factory.getQuickFix(target, violation));
        }
        return localQuickFixes;
    }

}
