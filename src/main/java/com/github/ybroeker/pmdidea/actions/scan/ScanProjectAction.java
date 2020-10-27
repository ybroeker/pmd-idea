package com.github.ybroeker.pmdidea.actions.scan;

import java.io.File;
import java.util.*;

import com.github.ybroeker.pmdidea.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ScanProjectAction extends AbstractScanAction {

    @Override
    protected List<File> getFiles(final @NotNull Project project) {
        //TODO: select which sources to include
        return project.getService(FileCollector.class).getFilesToScan(false);
    }

}
