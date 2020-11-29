package com.github.ybroeker.pmdidea.test;

import com.intellij.openapi.module.ModuleTypeId;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.testFramework.IdeaTestUtil;
import com.intellij.testFramework.LightProjectDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class JavaLightProjectDescriptor extends LightProjectDescriptor {

    @Override
    public @NotNull String getModuleTypeId() {
        return ModuleTypeId.JAVA_MODULE;
    }

    @Override
    public @Nullable Sdk getSdk() {
        return IdeaTestUtil.getMockJdk18();
    }

}
