package com.github.ybroeker.pmdidea.pmd;

import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PmdAdapterLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PmdAdapterLoader.class);

    private static final String IMPL = "com.github.ybroeker.pmdidea.pmdwrapper.PmdWrapperImpl";


    @NotNull
    public static PmdAdapter getInstance(PmdVersion version) {
        final List<URL> urls = PmdVersions.getUrls(version);
        final ClassLoader cl = new ChildFirstClassLoader(urls.toArray(new URL[0]), PmdAdapterLoader.class.getClassLoader());

        try {
            Class<?> clazz = cl.loadClass(IMPL);
            final PmdAdapter pmdAdapter = (PmdAdapter) clazz.getConstructor().newInstance();
            LOGGER.debug("Using PmdAdapter with version '{}'", pmdAdapter.getPmdVersion());
            return pmdAdapter;

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
