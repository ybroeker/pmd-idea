package com.github.ybroeker.pmdidea.actions.scan;

import java.io.File;
import java.util.*;

import com.github.ybroeker.pmdidea.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ScanCurrentFileAction extends AbstractScanAction {

    @Override
    protected List<File> getFiles(final @NotNull Project project) {
        return project.getService(FileCollector.class).getCurrentFile();
    }

}
