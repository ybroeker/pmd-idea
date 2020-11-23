package com.github.ybroeker.pmdidea.inspection;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.github.ybroeker.pmdidea.quickfix.QuickfixFactory;
import com.intellij.codeInspection.*;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public final class ProblemDescriptorFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProblemDescriptorFactory.class);

    private final Project project;

    public ProblemDescriptorFactory(final Project project) {
        this.project = project;
    }

    public ProblemDescriptor toProblemDescriptor(@NotNull InspectionManager inspectionManager, final PsiFile file, final PmdRuleViolation violation) {
        final PsiElement start = PsiElements.getElement(file, violation.getPosition().getBeginLine(), violation.getPosition().getBeginColumn());

        LOGGER.trace("Found PsiElement '{}' in '{}' at {}:{} ", start, file.getName(), violation.getPosition().getBeginLine(), violation.getPosition().getBeginColumn());

        return descriptor(inspectionManager, start, violation);
    }

    protected ProblemDescriptor descriptor(@NotNull final InspectionManager inspectionManager, final PsiElement target, final PmdRuleViolation violation) {
        return inspectionManager.createProblemDescriptor(
                target,
                getDescription(violation),
                getQuickFix(target, violation),
                ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                false);
    }

    private @Nullable LocalQuickFix getQuickFix(@NotNull final PsiElement target, @NotNull final PmdRuleViolation violation) {
        final QuickfixFactory service = project.getService(QuickfixFactory.class);
        return service.getQuickFix(target, violation);
    }

    private String getDescription(@NotNull final PmdRuleViolation violation) {
        return "[" + violation.getPmdRule().getName() + "]: " + violation.getMessage();
    }

}
