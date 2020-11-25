package com.github.ybroeker.pmdidea.pmd;


import java.io.File;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.intellij.openapi.components.Service;
import com.intellij.util.lang.UrlClassLoader;
import org.jetbrains.annotations.NotNull;

@Service
public final class PmdClassloaderFactory {

    private static final String PMD_CLASSES_DIR = "pmd/classes";

    private static final Pattern LIB_URL = Pattern.compile("^(.*?)[/\\\\]lib[/\\\\]?.*\\.jar$");

    @NotNull
    public List<URL> getUrls(final PmdVersion pmdVersion) {
        final List<String> pmdLibs = PmdVersions.getPmdLibs(pmdVersion);
        final String basePath = getBasePath();
        final List<URL> urls = new ArrayList<>();
        try {
            urls.add(new File(basePath, PMD_CLASSES_DIR).toURI().toURL());
            for (final String jar : pmdLibs) {
                urls.add(new File(basePath, jar).toURI().toURL());
            }
            return Collections.unmodifiableList(urls);
        } catch (MalformedURLException e) {
            throw new CouldNotLoadPmdException(e);
        }
    }

    private static List<URL> getBaseUrls() {
        final ClassLoader cl = PmdVersion.class.getClassLoader();
        if (cl instanceof UrlClassLoader) {
            return ((UrlClassLoader) cl).getUrls();
        }
        if (cl instanceof URLClassLoader) {
            return Arrays.asList(((URLClassLoader) cl).getURLs());
        }
        throw new AssertionError("Unknown ClassLoader: " + cl);
    }

    @NotNull
    private static String getBasePath() {
        final List<URL> urls = getBaseUrls();
        for (final URL url : urls) {
            final String path = getFilePathFromURL(url);
            final Matcher matcher = LIB_URL.matcher(path);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        throw new CouldNotLoadPmdException("Could not determine plugin directory");
    }

    @NotNull
    private static String getFilePathFromURL(final URL url) {
        try {
            return new File(url.toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new CouldNotLoadPmdException(e);
        }
    }

}
