package com.github.ybroeker.pmdidea.test;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.extension.*;

import static com.github.ybroeker.pmdidea.ResourceUtil.getInputStreamAsString;

public class ResourceResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.isAnnotated(Resource.class)
               && (parameterContext.getParameter().getType().isAssignableFrom(InputStream.class)
                   || parameterContext.getParameter().getType().isAssignableFrom(String.class)
                   || parameterContext.getParameter().getType().isAssignableFrom(URL.class)
                   || parameterContext.getParameter().getType().isAssignableFrom(File.class)
                   || parameterContext.getParameter().getType().isAssignableFrom(Path.class));
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
        try {
            final Resource resource = parameterContext.findAnnotation(Resource.class).get();
            final String value = resource.value();
            final Class<?> requiredType = parameterContext.getParameter().getType();
            final Class<?> testClass = extensionContext.getRequiredTestClass();
            final Method method = extensionContext.getTestMethod().orElse(null);
            final URL url = findResource(testClass, method, value);

            if (requiredType.isAssignableFrom(URL.class)) {
                return url;
            }
            if (requiredType.isAssignableFrom(InputStream.class)) {
                return url.openStream();
            }
            if (requiredType.isAssignableFrom(String.class)) {
                return getInputStreamAsString(url.openStream());
            }
            if (requiredType.isAssignableFrom(File.class)) {
                return Paths.get(url.toURI()).toFile();
            }
            if (requiredType.isAssignableFrom(Path.class)) {
                return Paths.get(url.toURI());
            }

            return new ParameterResolutionException("Could not load resource of type " + requiredType);
        } catch (IOException | URISyntaxException e) {
            throw new ParameterResolutionException("Could not resolve parameter", e);
        }
    }

    private URL findResource(Class<?> testClass, Method method, String value) {
        if (method != null) {
            URL methodResource = testClass.getResource(testClass.getSimpleName() + "/" + method.getName() + "/" + value);
            if (methodResource != null) {
                return methodResource;
            }
        }

        URL classResource = testClass.getResource(testClass.getSimpleName() + "/" + value);
        if (classResource != null) {
            return classResource;
        }

        URL packageResource = testClass.getResource(value);
        if (packageResource != null) {
            return packageResource;
        }

        URL rootResource = testClass.getResource("/" + value);
        return rootResource;
    }

}
