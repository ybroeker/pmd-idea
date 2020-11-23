package com.github.ybroeker.pmdidea.inspection;

import com.github.ybroeker.pmdidea.pmd.PmdRuleViolation;
import com.github.ybroeker.pmdidea.quickfix.DelegateQuickFixFactory;
import com.github.ybroeker.pmdidea.quickfix.QuickFixFactory;
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
        final PsiElement start = PsiFiles.getElement(file, violation.getPosition().getBeginLine(), violation.getPosition().getBeginColumn());

        LOGGER.trace("Found PsiElement '{}' in '{}' at {}:{} ", start, file.getName(), violation.getPosition().getBeginLine(), violation.getPosition().getBeginColumn());

        return descriptor(inspectionManager, start, violation);
    }

    protected ProblemDescriptor descriptor(@NotNull final InspectionManager inspectionManager, final PsiElement target, final PmdRuleViolation violation) {
        return inspectionManager.createProblemDescriptor(
                target,
                getDescription(violation),
                getQuickFix(target, violation),
                getProblemHighlightType(violation),
                false,
                false);
    }

    private ProblemHighlightType getProblemHighlightType(final PmdRuleViolation violation) {
        switch (violation.getPmdRule().getPmdRulePriority()) {
            case LOW:
                return ProblemHighlightType.WEAK_WARNING;
            case MEDIUM_LOW:
                return ProblemHighlightType.WEAK_WARNING;
            case MEDIUM:
                return ProblemHighlightType.GENERIC_ERROR_OR_WARNING;
            case MEDIUM_HIGH:
                return ProblemHighlightType.GENERIC_ERROR_OR_WARNING;
            case HIGH:
                return ProblemHighlightType.GENERIC_ERROR;
            default:
                throw new AssertionError("Unkown PmdRulePriority: "+violation);
        }
    }

    private @Nullable LocalQuickFix[] getQuickFix(@NotNull final PsiElement target, @NotNull final PmdRuleViolation violation) {
        final QuickFixFactory service = project.getService(DelegateQuickFixFactory.class);
        return service.getQuickFix(target, violation).toArray(new LocalQuickFix[0]);
    }

    private String getDescription(@NotNull final PmdRuleViolation violation) {
        return "[" + violation.getPmdRule().getName() + "]: " + violation.getMessage();
    }

}
