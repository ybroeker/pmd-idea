package com.github.ybroeker.pmdidea.pmd;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.intellij.util.lang.UrlClassLoader;
import org.jetbrains.annotations.NotNull;

public class PmdVersions {

    private static final String PROPERTIES = "/pmd-classpaths.properties";

    private static final String PMD_CLASSES_DIR = "pmd/classes";

    private static final Pattern LIB_URL = Pattern.compile("^(.*?)[/\\\\]lib[/\\\\]?.*\\.jar$");

    private static final Map<PmdVersion, List<URL>> CLASS_PATHS = loadProperties();


    public static List<PmdVersion> getVersions() {
        final ArrayList<PmdVersion> pmdVersions = new ArrayList<>(CLASS_PATHS.keySet());
        Collections.sort(pmdVersions);
        return Collections.unmodifiableList(pmdVersions);
    }

    public static PmdVersion getLatestVersion() {
        final List<PmdVersion> versions = getVersions();
        return versions.get(versions.size() - 1);
    }

    public static List<URL> getUrls(PmdVersion pmdVersion) {
        Objects.requireNonNull(pmdVersion, "pmdVersion");
        return Collections.unmodifiableList(CLASS_PATHS.get(pmdVersion));
    }

    @NotNull
    private static Map<PmdVersion, List<URL>> loadProperties() {
        final String basePath = getBasePath();

        try (InputStream is = PmdVersions.class.getResourceAsStream(PROPERTIES)) {
            final Properties properties = new Properties();
            properties.load(is);

            final HashMap<PmdVersion, List<URL>> versions = new HashMap<>();

            for (final String version : properties.stringPropertyNames()) {
                final List<URL> urls = new ArrayList<>();
                urls.add(toURL(new File(basePath, PMD_CLASSES_DIR)));
                for (String jar : properties.getProperty(version).trim().split("\\s*;\\s*")) {
                    urls.add(toURL(new File(basePath, jar)));
                }
                final PmdVersion pmdVersion = PmdVersion.of(version);
                versions.put(pmdVersion, urls);
            }

            return versions;
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read plugin-internal file: " + PROPERTIES, e);
        }
    }

    @NotNull
    private static String getBasePath() {
        final List<URL> urls = ((UrlClassLoader) PmdVersion.class.getClassLoader()).getUrls();
        for (final URL url : urls) {
            final String path = getFilePathFromURL(url);
            final Matcher matcher = LIB_URL.matcher(path);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        throw new RuntimeException("Could not determine plugin directory");
    }

    @NotNull
    private static URL toURL(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private static String getFilePathFromURL(URL url) {
        try {
            return new File(url.toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


}
