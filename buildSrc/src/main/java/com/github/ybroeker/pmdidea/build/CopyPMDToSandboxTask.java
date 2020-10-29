package com.github.ybroeker.pmdidea.build;

import java.io.File;

import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.Copy;


public class CopyPMDToSandboxTask extends Copy {

    public static final String NAME = "copyPMDToSandboxTask";
    public static final String TEST_NAME = "copyPMDToSandboxTestTask";

    public static final String TARGET_SUBFOLDER = "pmd/lib";

    private static final String SANDBOX_PATH = "idea-sandbox/plugins";

    private static final String TEST_SANDBOX_PATH = "idea-sandbox/plugins-test";

    public CopyPMDToSandboxTask() {
        setGroup("intellij");
        configureTask(false);
        final ResolvePmdArtifactsTask gatherTask = (ResolvePmdArtifactsTask) getProject().getTasks().getByName(ResolvePmdArtifactsTask.NAME);
        dependsOn(gatherTask);
        from(gatherTask.getBundledJarsDir());
    }


    private void configureTask(final boolean test) {
        String sandbox = (test ? TEST_SANDBOX_PATH : SANDBOX_PATH);

        setDescription("Adds the PMD artifacts to " + sandbox);
        into(new File(getProject().getBuildDir(), sandbox + "/PMD-IDEA/" + TARGET_SUBFOLDER));
    }


    public void setTest() {
        configureTask(true);
        final Project project = getProject();
        project.getTasks().getByName(JavaPlugin.TEST_TASK_NAME).dependsOn(this);
    }
}
