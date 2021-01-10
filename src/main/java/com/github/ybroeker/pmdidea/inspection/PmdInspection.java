package com.github.ybroeker.pmdidea.inspection;

import java.util.*;

import com.github.ybroeker.pmdidea.config.PmdConfigurationService;
import com.github.ybroeker.pmdidea.pmd.*;
import com.intellij.codeInspection.*;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PmdInspection extends LocalInspectionTool {

    private static final Logger LOGGER = LoggerFactory.getLogger(PmdInspection.class);

    @Override
    public ProblemDescriptor[] checkFile(@NotNull final PsiFile psiFile,
                                         @NotNull final InspectionManager manager,
                                         final boolean isOnTheFly) {
        LOGGER.trace("Try inspect File: '{}'", psiFile.getName());
        if (!psiFile.getFileType().equals(JavaFileType.INSTANCE)) {
            LOGGER.trace("Skip inspection, not a java file");
            return ProblemDescriptor.EMPTY_ARRAY;
        }

        final Project project = manager.getProject();
        final PmdConfigurationService service = project.getService(PmdConfigurationService.class);
        if (isOnTheFly && !service.getState().isRunOnTheFlyInspection()) {
            LOGGER.trace("Skip inspection, on the fly inspections disabled");
            return ProblemDescriptor.EMPTY_ARRAY;
        }

        if (!project.getService(RulesService.class).hasValidRuleSet()) {
            LOGGER.trace("Skip inspection, no rules configured");
            return ProblemDescriptor.EMPTY_ARRAY;
        }

        final ScannablePsiFile scannableFile = new ScannablePsiFile(psiFile);

        final List<PmdRuleViolation> violations = runPmd(project, scannableFile);
        return getProblemDescriptors(manager, psiFile, violations);
    }

    private List<PmdRuleViolation> runPmd(final Project project, final ScannablePsiFile scannableFile) {
        LOGGER.trace("Inspect File: '{}'", scannableFile.getDisplayName());

        final ViolationCollector pmdRunListener = new ViolationCollector();

        final PmdConfigurationFactory configurationFactory = project.getService(PmdConfigurationFactory.class);
        final PmdConfiguration configuration = configurationFactory.getPmdConfiguration(Collections.singletonList(scannableFile), pmdRunListener);

        final PmdAdapterDelegate pmdAdapter = project.getService(PmdAdapterDelegate.class);
        final long start = System.currentTimeMillis();
        pmdAdapter.runPmd(configuration);
        final long end = System.currentTimeMillis();

        LOGGER.trace("Inspected File: '{}' in {}ms", scannableFile.getDisplayName(), end-start);
        return pmdRunListener.getViolations();
    }

    private ProblemDescriptor[] getProblemDescriptors(@NotNull final InspectionManager manager, final PsiFile file, final List<PmdRuleViolation> violations) {
        final ProblemDescriptorFactory problemDescriptorFactory = manager.getProject().getService(ProblemDescriptorFactory.class);

        final List<ProblemDescriptor> problemDescriptors = new ArrayList<>();

        for (final PmdRuleViolation violation : violations) {
            final Optional<ProblemDescriptor> problemDescriptor = problemDescriptorFactory.toProblemDescriptor(manager, file, violation);
            problemDescriptor.ifPresent(problemDescriptors::add);
        }

        return problemDescriptors.toArray(new ProblemDescriptor[0]);
    }

}
