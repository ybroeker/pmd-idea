package com.github.ybroeker.pmdidea.inspection;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.intellij.codeInspection.*;
import com.intellij.openapi.components.Service;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public final class ProblemDescriptorFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProblemDescriptorFactory.class);

    public ProblemDescriptor toProblemDescriptor(@NotNull final InspectionManager inspectionManager, final PsiFile file, final PmdRuleViolation violation) {
        final PsiElement start = PsiElements.getElement(file, violation.getPosition().getBeginLine(), violation.getPosition().getBeginColumn());

        LOGGER.trace("Found PsiElement '{}' in '{}' at {}:{} ", start, file.getName(), violation.getPosition().getBeginLine(), violation.getPosition().getBeginColumn());

        return descriptor(inspectionManager, start, violation);
    }

    protected ProblemDescriptor descriptor(@NotNull final InspectionManager inspectionManager, final PsiElement target, final PmdRuleViolation violation) {
        return inspectionManager.createProblemDescriptor(
                target,
                getDescription(violation),
                (LocalQuickFix) null,
                ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                false);
    }

    private String getDescription(@NotNull final PmdRuleViolation violation) {
        return "[" + violation.getPmdRule().getName() + "]: " + violation.getMessage();
    }

}
