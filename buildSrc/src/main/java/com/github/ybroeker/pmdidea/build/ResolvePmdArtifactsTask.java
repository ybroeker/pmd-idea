package com.github.ybroeker.pmdidea.build;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.*;

import groovy.lang.Closure;
import org.apache.commons.io.FileUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.language.base.plugins.LifecycleBasePlugin;


public class ResolvePmdArtifactsTask extends DefaultTask {

    public static final String NAME = "resolvePmdArtifacts";

    private final File bundledJarsDir;

    private final File classPathsInfoFile;


    public ResolvePmdArtifactsTask() {
        setGroup(LifecycleBasePlugin.BUILD_GROUP);
        setDescription("Resolves PMD artifacts");
        final Project project = getProject();


        final PmdVersions csVersions = new PmdVersions(project);
        getInputs().file(csVersions.getPropertyFile());


        bundledJarsDir = getTemporaryDir();
        classPathsInfoFile = new File(project.getBuildDir(), "resources-generated/pmd-classpaths.properties");
        getOutputs().dir(bundledJarsDir);
        getOutputs().file(classPathsInfoFile);


        SourceSetContainer sourceSets = (SourceSetContainer) project.getProperties().get("sourceSets");
        SourceSet mainSourceSet = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME);
        mainSourceSet.getResources().srcDir(classPathsInfoFile.getParentFile());


        project.afterEvaluate(new Closure<Void>(this) {
            @Override
            public Void call(final Object... args) {
                project.getTasks().getByName(JavaPlugin.PROCESS_RESOURCES_TASK_NAME).dependsOn(getOwner());
                return null;
            }
        });


        doLast(new Closure<Void>(this) {
            @Override
            public Void call() {
                final Set<File> bundledFiles = new TreeSet<>();
                final Properties classPaths = new Properties();

                for (final String csVersion : csVersions.getVersions()) {
                    final Set<File> files = resolveDependencies(project, csVersion);
                    classPaths.setProperty(csVersion, convertToClassPath(files));
                    bundledFiles.addAll(files);
                }
                copyFiles(bundledFiles);
                createClassPathsFile(classPaths);
                return null;
            }
        });
    }


    private Set<File> resolveDependencies(final Project project, final String version) {
        final String gradleVersion = version.replaceAll("\\.", "_");
        final Configuration gradleConfig = project.getConfigurations().create("pmd_" + gradleVersion);
        final Dependency csDep = project.getDependencies().create("net.sourceforge.pmd:pmd-java:" + version);
        gradleConfig.getDependencies().add(csDep);
        final Set<File> files = gradleConfig.resolve();
        project.getConfigurations().remove(gradleConfig);
        return files;
    }


    private String convertToClassPath(final Set<File> dependencies) {
        final StringJoiner stringJoiner = new StringJoiner(";");
        for (final File dependency : dependencies) {
            stringJoiner.add(CopyPMDToSandboxTask.TARGET_SUBFOLDER + "/" + dependency.getName());
        }
        return stringJoiner.toString();
    }


    private void copyFiles(final Set<File> jars) {
        for (final File jar : jars) {
            try {
                FileUtils.copyFileToDirectory(jar, bundledJarsDir, true);
            } catch (IOException e) {
                throw new GradleException("Unable to copy file: " + jar.getAbsolutePath(), e);
            }
        }
    }


    private void createClassPathsFile(final Properties pClassPaths) {
        try {
            Files.createDirectories(classPathsInfoFile.getParentFile().toPath());
            try (final OutputStream os = Files.newOutputStream(classPathsInfoFile.toPath())) {
                pClassPaths.store(os, " Class path information for PMD artifacts bundled with PMD-IDEA");
            }
        } catch (IOException e) {
            throw new GradleException(
                    "Unable to write classpath info file: " + classPathsInfoFile.getAbsolutePath(), e);
        }
    }

    public File getBundledJarsDir() {
        return bundledJarsDir;
    }

}
