package com.github.ybroeker.pmdidea.pmd;

import java.io.File;
import java.nio.file.*;
import java.util.*;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.OrderEnumerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public final class AuxClassPathFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuxClassPathFactory.class);

    private final Project project;

    public AuxClassPathFactory(final Project project) {
        this.project = project;
    }


    public String getClassPath() {
        final List<Path> paths = getClassPathEntries();

        return joinClassPathEntries(paths);
    }

    private List<Path> getClassPathEntries() {
        final Module[] modules = ModuleManager.getInstance(project).getModules();

        final List<Path> paths = new ArrayList<>();

        for (final Module module : modules) {
            final List<String> pathList = OrderEnumerator.orderEntries(module).recursively().getPathsList().getPathList();
            for (final String pathString : pathList) {
                final Path path = Paths.get(pathString);
                if (Files.exists(path)) {
                    paths.add(path);
                    LOGGER.trace("Include in Classpath: {}", path);
                } else {
                    LOGGER.trace("Exclude from Classpath: {}", path);
                }
            }
        }
        return paths;
    }

    private String joinClassPathEntries(final List<Path> paths) {
        final StringJoiner joiner = new StringJoiner(File.pathSeparator);
        for (final Path path : paths) {
            joiner.add(path.toAbsolutePath().toString());
        }
        return joiner.toString();
    }


}
