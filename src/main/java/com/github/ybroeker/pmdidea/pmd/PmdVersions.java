package com.github.ybroeker.pmdidea.pmd;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.jetbrains.annotations.NotNull;

public class PmdVersions {

    private static final String PROPERTIES = "/pmd-classpaths.properties";

    private static final Map<PmdVersion, List<String>> PMD_LIBS = loadProperties();


    public static List<PmdVersion> getVersions() {
        final ArrayList<PmdVersion> pmdVersions = new ArrayList<>(PMD_LIBS.keySet());
        Collections.sort(pmdVersions);
        return Collections.unmodifiableList(pmdVersions);
    }

    public static PmdVersion getLatestVersion() {
        final List<PmdVersion> versions = getVersions();
        return versions.get(versions.size() - 1);
    }

    public static List<String> getPmdLibs(final PmdVersion pmdVersion) {
        Objects.requireNonNull(pmdVersion, "pmdVersion");
        return Collections.unmodifiableList(PMD_LIBS.get(pmdVersion));

    }

    @NotNull
    private static Map<PmdVersion, List<String>> loadProperties() {
        try (InputStream is = PmdVersions.class.getResourceAsStream(PROPERTIES)) {
            final Properties properties = new Properties();
            properties.load(is);

            final HashMap<PmdVersion, List<String>> versions = new HashMap<>();

            for (final String version : properties.stringPropertyNames()) {
                final List<String> strings = Arrays.asList(properties.getProperty(version).trim().split("\\s*;\\s*"));
                final List<String> libs = new ArrayList<>(strings);
                final PmdVersion pmdVersion = PmdVersion.of(version);
                versions.put(pmdVersion, libs);
            }

            return versions;
        } catch (IOException e) {
            throw new CouldNotLoadPmdException("Could not read plugin-internal file: " + PROPERTIES, e);
        }
    }

}
