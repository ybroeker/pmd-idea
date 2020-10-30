package com.github.ybroeker.pmdidea.config;

import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.logging.Logger;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.util.lang.JavaVersion;
import org.jetbrains.jps.model.java.LanguageLevel;

public class JavaVersions {

    public static final JavaVersion DEFAULT = JavaVersion.compose(8);

    public static Optional<JavaVersion> getProjectJavaVersion(Project project) {
        Sdk projectSdk = ProjectRootManager.getInstance(project).getProjectSdk();
        if (projectSdk == null) {
            return Optional.empty();
        }
        final String versionString = projectSdk.getVersionString();
        if (versionString == null) {
            return Optional.empty();
        }
        final JavaVersion parse = JavaVersion.tryParse(versionString);
        if (parse == null) {
            return Optional.empty();
        }
        final JavaVersion compose = JavaVersion.compose(parse.feature);
        return Optional.of(compose);
    }

    public static JavaVersion getProjectVersionOrDefault(Project project) {
        return JavaVersions.getProjectJavaVersion(project).orElse(JavaVersions.DEFAULT);
    }

    public static JavaVersion[] getAllJavaVersions() {
        final LanguageLevel[] values = LanguageLevel.values();
        final JavaVersion[] javaVersions = new JavaVersion[values.length];
        for (int i = 0; i < values.length; i++) {
            final LanguageLevel value = values[i];
            javaVersions[i] = value.toJavaVersion();
        }
        return javaVersions;
    }


}
