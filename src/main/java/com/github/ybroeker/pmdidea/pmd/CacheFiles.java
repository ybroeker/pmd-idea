package com.github.ybroeker.pmdidea.pmd;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.logging.Logger;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class CacheFiles {

    @NotNull
    public static Optional<File> getCacheFile(final Project project) {
        return getCacheFolder(project).map(folder -> new File(folder, "pmd.tmp"));
    }

    @NotNull
    private static Optional<File> getCacheFolder(final Project project) {
        if (project.getProjectFile() != null) {
            final VirtualFile possibleIdeaFolder = project.getProjectFile().getParent();
            return Optional.of(new File(possibleIdeaFolder.getPath()));
        }
        if (project.getBasePath() != null) {
            final String basePath = project.getBasePath();
            return Optional.of(new File(basePath));
        }
        return Optional.empty();
    }



}
