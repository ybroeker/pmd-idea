package com.github.ybroeker.pmdidea.build;

import java.io.File;

import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.SourceSet;


public class CopyClassesToSandboxTask extends Copy {

    public static final String NAME = "copyClassesToSandboxTask";

    public static final String TEST_NAME = "copyClassesToSandboxTestTask";

    private static final String WRAPPER_SOURCESET_NAME = "pmdwrapper";

    private static final String TARGET_SUBFOLDER = "pmd/classes";

    private static final String SANDBOX_PATH = "idea-sandbox/plugins";

    private static final String TEST_SANDBOX_PATH = "idea-sandbox/plugins-test";


    public CopyClassesToSandboxTask() {
        setGroup("intellij");
        dependsOn(getProject().getTasks().getByName(WRAPPER_SOURCESET_NAME + "Classes"));
        JavaPluginConvention javaConvention = getProject().getConvention().getPlugin(JavaPluginConvention.class);
        SourceSet sourceSet = javaConvention.getSourceSets().getByName(WRAPPER_SOURCESET_NAME);
        from(sourceSet.getOutput());
        configureTask(false);
    }


    private void configureTask(final boolean test) {
        String sandbox = (test ? TEST_SANDBOX_PATH : SANDBOX_PATH);

        setDescription("Copy classes from '" + WRAPPER_SOURCESET_NAME + "' sourceset into " + sandbox);
        into(new File(getProject().getBuildDir(),  sandbox + "/pmd-idea/" + TARGET_SUBFOLDER));
    }


    public void setTest() {
        configureTask(true);
        final Project project = getProject();
        project.getTasks().getByName(JavaPlugin.TEST_TASK_NAME).dependsOn(this);
    }
}
