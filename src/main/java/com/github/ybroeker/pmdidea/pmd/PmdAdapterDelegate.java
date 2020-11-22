package com.github.ybroeker.pmdidea.pmd;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;

import com.intellij.ide.plugins.cl.PluginClassLoader;
import com.intellij.openapi.components.Service;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public final class PmdAdapterDelegate implements PmdAdapter {

    private static final String IMPL = "com.github.ybroeker.pmdidea.pmdwrapper.PmdWrapperImpl";

    private static final Logger LOGGER = LoggerFactory.getLogger(PmdAdapterDelegate.class);

    private PmdAdapter delegate;

    @Override
    public PmdVersion getPmdVersion() {
        return delegate.getPmdVersion();
    }

    @Override
    public void runPmd(final PmdConfiguration pmdConfiguration) {
        if (delegate == null || !delegate.getPmdVersion().equals(pmdConfiguration.getPmdOptions().getPmdVersion())) {
            this.delegate = getInstance(pmdConfiguration.getPmdOptions().getPmdVersion());
        }
        System.out.println(this.delegate.getPmdVersion());
        this.delegate.runPmd(pmdConfiguration);
    }

    @NotNull
    private PmdAdapter getInstance(PmdVersion version) {
        final List<URL> urls = PmdVersions.getUrls(version);
        final PluginClassLoader parentClassLoader = (PluginClassLoader) PmdAdapterDelegate.class.getClassLoader();
        final ClassLoader classLoader = new ChildFirstClassLoader(urls.toArray(new URL[0]), parentClassLoader);

        try {
            Class<?> clazz = classLoader.loadClass(IMPL);
            final PmdAdapter pmdAdapter = (PmdAdapter) clazz.getConstructor().newInstance();
            LOGGER.debug("Using PmdAdapter with version '{}'", pmdAdapter.getPmdVersion());
            return pmdAdapter;

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
