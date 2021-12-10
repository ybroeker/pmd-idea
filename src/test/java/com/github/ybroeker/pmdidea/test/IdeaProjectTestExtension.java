package com.github.ybroeker.pmdidea.test;

import java.lang.reflect.Method;

import com.intellij.testFramework.EdtTestUtil;
import com.intellij.testFramework.TestRunnerUtil;
import com.intellij.testFramework.fixtures.*;
import com.intellij.testFramework.fixtures.impl.JavaCodeInsightTestFixtureImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;


public class IdeaProjectTestExtension implements ParameterResolver, InvocationInterceptor {

    static {
        TestRunnerUtil.replaceIdeEventQueueSafely();
    }

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext,
                                     final ExtensionContext extensionContext) {
        final Class<?> type = parameterContext.getParameter().getType();
        if (type.isAssignableFrom(LightIdeaTestFixture.class)) {
            return true;
        }
        if (type.isAssignableFrom(CodeInsightTestFixture.class)) {
            return true;
        }
        if (type.isAssignableFrom(JavaCodeInsightTestFixture.class)) {
            return true;
        }


        return false;
    }

    static class IdeaProjectTestFixtureCloseable implements ExtensionContext.Store.CloseableResource {
        IdeaProjectTestFixture ideaProjectTestFixture;

        public IdeaProjectTestFixtureCloseable(final IdeaProjectTestFixture ideaProjectTestFixture) {
            this.ideaProjectTestFixture = ideaProjectTestFixture;
        }

        @Override
        public void close() throws Throwable {
            EdtTestUtil.runInEdtAndWait(ideaProjectTestFixture::tearDown);
        }
    }

    @Override
    public IdeaProjectTestFixture resolveParameter(final ParameterContext parameterContext,
                                                   final ExtensionContext extensionContext) {

        final IdeaProjectTestFixtureCloseable ideaProjectTestFixture = extensionContext
                .getStore(Namespace.GLOBAL)
                .get(IdeaProjectTestFixture.class, IdeaProjectTestFixtureCloseable.class);

        if (ideaProjectTestFixture != null) {
            return ideaProjectTestFixture.ideaProjectTestFixture;
        }


        final IdeaProjectTestFixture fixture = createFixture(parameterContext);
        try {
            EdtTestUtil.runInEdtAndWait(fixture::setUp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        extensionContext
                .getStore(Namespace.GLOBAL)
                .put(IdeaProjectTestFixture.class, new IdeaProjectTestFixtureCloseable(fixture));

        return fixture;
    }

    @NotNull
    private IdeaProjectTestFixture createFixture(final ParameterContext parameterContext) {
        final Class<?> type = parameterContext.getParameter().getType();
        final IdeaProjectTestFixture fixture;
        if (type.isAssignableFrom(LightIdeaTestFixture.class)) {
            fixture = buildFixture();
        } else if (type.isAssignableFrom(CodeInsightTestFixture.class)) {
            fixture = buildCodeInsightTestFixture();
        } else if (type.isAssignableFrom(JavaCodeInsightTestFixture.class)) {
            fixture = buildJavaCodeInsightTestFixture();
        } else {
            throw new ParameterResolutionException("Could not create " + type);
        }
        return fixture;
    }

    @NotNull
    private IdeaProjectTestFixture buildFixture() {
        return IdeaTestFixtureFactory.getFixtureFactory().createLightFixtureBuilder().getFixture();
    }

    @NotNull
    private CodeInsightTestFixture buildCodeInsightTestFixture() {
        final IdeaProjectTestFixture fixture = IdeaTestFixtureFactory.getFixtureFactory().createLightFixtureBuilder().getFixture();
        return IdeaTestFixtureFactory.getFixtureFactory().createCodeInsightFixture(fixture);
    }

    @NotNull
    private JavaCodeInsightTestFixture buildJavaCodeInsightTestFixture() {
        final IdeaProjectTestFixture fixture = IdeaTestFixtureFactory.getFixtureFactory().createLightFixtureBuilder(new JavaLightProjectDescriptor()).getFixture();
        final TempDirTestFixture tempDirTestFixture = IdeaTestFixtureFactory.getFixtureFactory().createTempDirTestFixture();
        return new JavaCodeInsightTestFixtureImpl(fixture, tempDirTestFixture);
    }


    @Override
    public void interceptBeforeEachMethod(final Invocation<Void> invocation, final ReflectiveInvocationContext<Method> invocationContext, final ExtensionContext extensionContext) throws Throwable {
        EdtTestUtil.runInEdtAndWait(() -> InvocationInterceptor.super.interceptBeforeEachMethod(invocation, invocationContext, extensionContext));
    }

    @Override
    public void interceptAfterEachMethod(final Invocation<Void> invocation, final ReflectiveInvocationContext<Method> invocationContext, final ExtensionContext extensionContext) throws Throwable {
        EdtTestUtil.runInEdtAndWait(() -> InvocationInterceptor.super.interceptAfterEachMethod(invocation, invocationContext, extensionContext));
    }

    @Override
    public void interceptTestMethod(final Invocation<Void> invocation, final ReflectiveInvocationContext<Method> invocationContext, final ExtensionContext extensionContext) throws Throwable {
        EdtTestUtil.runInEdtAndWait(() -> InvocationInterceptor.super.interceptTestMethod(invocation, invocationContext, extensionContext));
    }

    @Override
    public <T> T interceptTestFactoryMethod(final Invocation<T> invocation, final ReflectiveInvocationContext<Method> invocationContext, final ExtensionContext extensionContext) throws Throwable {
        return EdtTestUtil.runInEdtAndGet(() -> InvocationInterceptor.super.interceptTestFactoryMethod(invocation, invocationContext, extensionContext));
    }

    @Override
    public void interceptTestTemplateMethod(final Invocation<Void> invocation, final ReflectiveInvocationContext<Method> invocationContext, final ExtensionContext extensionContext) throws Throwable {
        EdtTestUtil.runInEdtAndWait(() -> InvocationInterceptor.super.interceptTestTemplateMethod(invocation, invocationContext, extensionContext));
    }

    @Override
    public void interceptDynamicTest(final Invocation<Void> invocation, final ExtensionContext extensionContext) throws Throwable {
        EdtTestUtil.runInEdtAndWait(() -> InvocationInterceptor.super.interceptDynamicTest(invocation, extensionContext));

    }

}
