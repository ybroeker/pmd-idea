package com.github.ybroeker.pmdidea.inspection;

import java.nio.file.*;
import java.util.*;

import com.github.ybroeker.pmdidea.config.PmdConfigurationService;
import com.github.ybroeker.pmdidea.pmd.*;
import com.intellij.codeInspection.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PmdInspection extends LocalInspectionTool {

    private static final Logger LOGGER = LoggerFactory.getLogger(PmdInspection.class);

    private Optional<Path> getRules(final Project project) {
        final PmdConfigurationService service = project.getService(PmdConfigurationService.class);
        final String pathName = service.getState().getRulesPath();
        if (pathName == null || pathName.isEmpty()) {
            return Optional.empty();
        }
        final Path rulesPath = Paths.get(pathName);
        if (!Files.exists(rulesPath)) {
            return Optional.empty();
        }
        return Optional.of(rulesPath);
    }


    @Override
    public ProblemDescriptor[] checkFile(@NotNull final PsiFile psiFile,
                                         @NotNull final InspectionManager manager,
                                         final boolean isOnTheFly) {
        LOGGER.debug("Inspect File: '{}'", psiFile);
        final Project project = manager.getProject();

        final Optional<Path> rulesPath = getRules(project);
        if (!rulesPath.isPresent()) {
            LOGGER.debug("Skip inspection, no rules configured");
            return new ProblemDescriptor[0];
        }

        final VirtuallyFile virtuallyFile = new VirtuallyFile(psiFile);

        final PmdConfigurationService service = project.getService(PmdConfigurationService.class);

        final PmdOptions pmdOptions = new PmdOptions(service.getState().getJdkVersion().toString(), service.getState().getPmdVersion());

        final ViolationCollector pmdRunListener = new ViolationCollector();

        final PmdConfiguration configuration = new PmdConfiguration(project, Collections.singletonList(virtuallyFile), rulesPath.get().toFile().getAbsolutePath(), pmdOptions, pmdRunListener);

        final PmdAdapterDelegate pmdAdapter = project.getService(PmdAdapterDelegate.class);
        pmdAdapter.runPmd(configuration);

        return getProblemDescriptors(manager, psiFile, pmdRunListener.getViolations());
    }

    private ProblemDescriptor[] getProblemDescriptors(@NotNull final InspectionManager manager, final PsiFile file, final List<PmdRuleViolation> violations) {
        final ProblemDescriptorFactory problemDescriptorFactory = manager.getProject().getService(ProblemDescriptorFactory.class);

        List<ProblemDescriptor> problemDescriptors = new ArrayList<>();

        for (final PmdRuleViolation violation : violations) {
            problemDescriptors.add(problemDescriptorFactory.toProblemDescriptor(manager, file, violation));
        }

        return problemDescriptors.toArray(new ProblemDescriptor[0]);
    }

}
