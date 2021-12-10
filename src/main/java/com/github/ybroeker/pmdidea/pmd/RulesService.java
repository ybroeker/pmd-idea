package com.github.ybroeker.pmdidea.pmd;

import java.nio.file.*;
import java.util.Optional;

import com.github.ybroeker.pmdidea.config.PmdConfigurationService;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;

@Service
public final class RulesService {

    private static final String BASIC_RULES = "rulesets/java/basic.xml";

    private final Project project;

    public RulesService(final Project project) {
        this.project = project;
    }


    public String getRulesPath() {
        return getRules().orElseThrow(() -> new IllegalStateException("No valid ruleset found!"));
    }

    public boolean hasValidRuleSet() {
        return getRules().isPresent();
    }


    private Optional<String> getRules() {
        final PmdConfigurationService service = project.getService(PmdConfigurationService.class);
        final String pathName = service.getState().getRulesPath();
        if (pathName == null || pathName.isEmpty()) {
            return Optional.of(BASIC_RULES);
        }
        final Path rulesPath = Paths.get(pathName);
        if (!Files.exists(rulesPath)) {
            return Optional.empty();
        }
        return Optional.of(pathName);
    }

}
