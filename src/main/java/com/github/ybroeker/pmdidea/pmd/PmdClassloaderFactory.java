package com.github.ybroeker.pmdidea.pmd;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import com.github.ybroeker.pmdidea.PmdPlugin;
import com.intellij.ide.plugins.*;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.extensions.PluginId;
import org.jetbrains.annotations.NotNull;

@Service
public final class PmdClassloaderFactory {

    private static final String PMD_CLASSES_DIR = "pmd/classes";

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

    @NotNull
    private static String getBasePath() {
        final IdeaPluginDescriptor plugin = PluginManagerCore.getPlugin(PluginId.getId(PmdPlugin.PLUGIN_ID));
        if (plugin == null) {
            throw new AssertionError("Plugin not found: " + PmdPlugin.PLUGIN_ID);
        }
        return plugin.getPluginPath().toAbsolutePath().toString();
    }

}
