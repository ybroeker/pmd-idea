package com.github.ybroeker.pmdidea.pmd;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.intellij.util.lang.UrlClassLoader;
import org.jetbrains.annotations.NotNull;

public class PmdAdapterLocator {

    private static final String IMPL = "com.github.ybroeker.pmdidea.pmdwrapper.PmdWrapperImpl";

    private static final String PROPERTIES = "/pmd-classpaths.properties";

    private static final Pattern LIB_URL = Pattern.compile("^(.*?)[/\\\\]lib[/\\\\]?.*\\.jar$");


    public static List<PmdVersion> getAvailableVersions() {
        final Properties properties = loadProperties();

        List<PmdVersion> result = new ArrayList<>();
        for (final Object o : properties.keySet()) {
            result.add(PmdVersion.of((String) o));
        }
        Collections.sort(result);

        return result;
    }

    public static PmdVersion getLatestVersion() {
        final List<PmdVersion> availableVersions = PmdAdapterLocator.getAvailableVersions();

        final PmdVersion latestVersion = availableVersions.get(availableVersions.size() - 1);
        return latestVersion;
    }

    @NotNull
    public static PmdAdapter getInstance(String version) {
        final Properties result = loadProperties();
        final String basePath = getBasePath();

        final List<URL> urls = new ArrayList<>();
        urls.add(toURL(new File(basePath, "pmd/classes")));
        for (String jar : result.getProperty(version).trim().split("\\s*;\\s*")) {
            urls.add(toURL(new File(basePath, jar)));
        }

        final ClassLoader cl = new ChildFirstClassLoader(urls.toArray(new URL[0]), PmdAdapterLocator.class.getClassLoader());

        try {
            Class<?> clazz = cl.loadClass(IMPL);
            return (PmdAdapter) clazz.getConstructor().newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private static Properties loadProperties() {
        try (InputStream is = PmdAdapterLocator.class.getResourceAsStream(PROPERTIES)) {
            final Properties result = new Properties();
            result.load(is);
            return result;
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read plugin-internal file: " + PROPERTIES, e);
        }
    }

    @NotNull
    private static String getBasePath() {
        final List<URL> urls = ((UrlClassLoader) PmdAdapterLocator.class.getClassLoader()).getUrls();
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
