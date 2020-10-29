package com.github.ybroeker.pmdidea.build;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import org.gradle.api.GradleException;
import org.gradle.api.Project;


public class PmdVersions {

    private static final String PROPERTY_FILE = "src/main/resources/pmd-versions.properties";

    private static final String VERSION_PROPERTY = "pmd.versions";

    private final Path propertiesPath;

    private final Set<String> versions;

    public PmdVersions(final Project project) {
        propertiesPath = project.getProjectDir().toPath().resolve(PROPERTY_FILE);
        versions = createVersions();
    }


    private Set<String> createVersions() {
        final Properties props = getProperties();
        return createVersions(props);
    }


    private Properties getProperties() {
        final Properties props = new Properties();
        try (InputStream is = Files.newInputStream(propertiesPath)) {
            props.load(is);
        } catch (SecurityException | IOException e) {
            throw new GradleException("Error reading configuration file '" + propertiesPath + "' during build.", e);
        }
        return props;
    }


    private Set<String> createVersions(final Properties props) {
        final String propertyValue = props.getProperty(VERSION_PROPERTY);
        if (propertyValue == null || propertyValue.trim().isEmpty()) {
            throw new GradleException(
                    "Property '" + VERSION_PROPERTY + "' missing from configuration file '" + PROPERTY_FILE + "'");
        }

        final String[] versions = propertyValue.trim().split("\\s*,\\s*");
        final Set<String> result = new HashSet<>();
        for (final String version : versions) {
            if (!version.isEmpty()) {
                result.add(version);
            }
        }

        if (result.isEmpty()) {
            throw new GradleException(
                    "Property '" + VERSION_PROPERTY + "' was empty in configuration file '" + PROPERTY_FILE + "'");
        }
        return result;
    }


    public Path getPropertyFile() {
        return propertiesPath;
    }


    public Set<String> getVersions() {
        return versions;
    }
}
