package com.github.ybroeker.pmdidea.pmd;

import java.io.File;
import java.util.List;

import com.intellij.openapi.project.Project;

public class PmdConfiguration {

    private final Project project;

    private final List<ScannableFile> files;

    private final String ruleSets;

    private final PmdOptions pmdOptions;

    private final PmdRunListener pmdRunListener;

    public PmdConfiguration(final Project project, final List<ScannableFile> files, final String ruleSets, final PmdOptions pmdOptions, final PmdRunListener pmdRunListener) {
        this.project = project;
        this.files = files;
        this.ruleSets = ruleSets;
        this.pmdOptions = pmdOptions;
        this.pmdRunListener = pmdRunListener;
    }

    public Project getProject() {
        return project;
    }

    public List<ScannableFile> getFiles() {
        return files;
    }

    public String getRuleSets() {
        return ruleSets;
    }

    public PmdOptions getPmdOptions() {
        return pmdOptions;
    }

    public PmdRunListener getPmdRunListener() {
        return pmdRunListener;
    }
}
