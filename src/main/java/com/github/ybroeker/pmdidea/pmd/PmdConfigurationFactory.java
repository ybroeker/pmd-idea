package com.github.ybroeker.pmdidea.pmd;

import java.util.List;

import com.github.ybroeker.pmdidea.config.PmdConfigurationService;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;


@Service
public final class PmdConfigurationFactory {

    private final Project project;

    public PmdConfigurationFactory(final Project project) {
        this.project = project;
    }

    public PmdConfiguration getPmdConfiguration(final List<ScannableFile> files, final PmdRunListener pmdRunListener) {
        final RulesService rulesService = project.getService(RulesService.class);
        final PmdConfigurationService service = project.getService(PmdConfigurationService.class);
        final PmdOptions pmdOptions = new PmdOptions(service.getState().getJdkVersion().toString(), service.getState().getPmdVersion());
        final String classPath = project.getService(AuxClassPathFactory.class).getClassPath();

        return new PmdConfiguration(project, files, rulesService.getRulesPath(), pmdOptions, pmdRunListener, classPath);
    }

}
