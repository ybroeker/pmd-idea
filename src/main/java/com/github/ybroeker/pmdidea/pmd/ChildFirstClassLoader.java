package com.github.ybroeker.pmdidea.pmd;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import org.jetbrains.annotations.NotNull;

public class ChildFirstClassLoader extends URLClassLoader {

    private final @NotNull ClassLoader sysClzLoader;

    public ChildFirstClassLoader(final URL[] urls, final ClassLoader parent) {
        super(urls, parent);
        sysClzLoader = Objects.requireNonNull(getSystemClassLoader(), "sysClzLoader");
    }

    @Override
    protected Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            final Class<?> loadedClass = loadClassInternal(name, resolve);

            if (resolve) {
                resolveClass(loadedClass);
            }
            return loadedClass;
        }
    }

    private Class<?> loadClassInternal(final String name, final boolean parentShouldResolve) throws ClassNotFoundException {
        Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass != null) {
            return loadedClass;
        }

        try {
            loadedClass = sysClzLoader.loadClass(name);
            if (loadedClass != null) {
                return loadedClass;
            }
        } catch (ClassNotFoundException ignored) {
            //Search in own urls...
        }

        try {
            loadedClass = findClass(name);
            if (loadedClass != null) {
                return loadedClass;
            }
        } catch (ClassNotFoundException ignored) {
            //Search in parent...
        }
        return super.loadClass(name, parentShouldResolve);
    }

    @Override
    public Enumeration<URL> getResources(final String name) throws IOException {
        final List<URL> allRes = new LinkedList<>();

        final Enumeration<URL> sysResources = sysClzLoader.getResources(name);
        if (sysResources != null) {
            while (sysResources.hasMoreElements()) {
                allRes.add(sysResources.nextElement());
            }
        }

        final Enumeration<URL> thisRes = findResources(name);
        if (thisRes != null) {
            while (thisRes.hasMoreElements()) {
                allRes.add(thisRes.nextElement());
            }
        }

        final Enumeration<URL> parentRes = super.findResources(name);
        if (parentRes != null) {
            while (parentRes.hasMoreElements()) {
                allRes.add(parentRes.nextElement());
            }
        }

        return new IteratorEnumeration<>(allRes.iterator());
    }

    private static class IteratorEnumeration<T> implements Enumeration<T> {

        private final Iterator<T> it;

        public IteratorEnumeration(final Iterator<T> it) {
            this.it = it;
        }

        @Override
        public boolean hasMoreElements() {
            return it.hasNext();
        }

        @Override
        public T nextElement() {
            return it.next();
        }
    }


    @Override
    public URL getResource(final String name) {
        URL res = sysClzLoader.getResource(name);
        if (res == null) {
            res = findResource(name);
        }
        if (res == null) {
            res = super.getResource(name);
        }
        return res;
    }

}
