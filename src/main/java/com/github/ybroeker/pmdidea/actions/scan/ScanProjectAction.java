package com.github.ybroeker.pmdidea.actions.scan;

import java.io.File;
import java.util.*;

import com.github.ybroeker.pmdidea.*;
import com.github.ybroeker.pmdidea.config.PmdConfigurationService;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ScanProjectAction extends AbstractScanAction {

    @Override
    protected List<File> getFiles(final @NotNull Project project) {
        final PmdConfigurationService service = project.getService(PmdConfigurationService.class);

        return project.getService(FileCollector.class).getFilesToScan(service.getState().isCheckTests());
    }

}
