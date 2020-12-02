package com.github.ybroeker.pmdidea.config;


import java.util.*;

import com.github.ybroeker.pmdidea.PmdPlugin;
import com.github.ybroeker.pmdidea.pmd.*;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.lang.JavaVersion;
import com.intellij.util.xmlb.annotations.OptionTag;
import org.jetbrains.annotations.NotNull;

@State(name = PmdPlugin.PLUGIN_ID, storages = {@Storage("pmd-idea.xml")})
public class PmdConfigurationService implements PersistentStateComponent<PmdConfigurationService.State> {

    private State state;

    private final Project project;

    public PmdConfigurationService(Project project) {
        this.project = project;
    }

    @Override
    public @NotNull PmdConfigurationService.State getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull final State state) {
        this.state = state;
    }

    @Override
    public void noStateLoaded() {
        final PmdVersion latestVersion = PmdVersions.getLatestVersion();
        final JavaVersion javaVersion = JavaVersions.getProjectVersionOrDefault(project);

        this.state = new State();
        this.state.pmdVersion = latestVersion;
        this.state.jdkVersion = javaVersion;
    }

    public void setState(final State state) {
        this.state = state;
    }

    public static class State {

        @OptionTag
        private String rulesPath = null;

        @OptionTag
        private boolean checkTests = false;

        @OptionTag(converter = PmdVersionConverter.class)
        private PmdVersion pmdVersion;

        @OptionTag(converter = JavaVersionConverter.class)
        private JavaVersion jdkVersion;

        @OptionTag
        private boolean runOnTheFlyInspection = true;

        public State() {
        }

        public State(final String rulesPath, final boolean checkTests, final PmdVersion pmdVersion, final JavaVersion jdkVersion, final boolean runOnTheFlyInspection) {
            this.rulesPath = rulesPath;
            this.checkTests = checkTests;
            this.pmdVersion = pmdVersion;
            this.jdkVersion = jdkVersion;
            this.runOnTheFlyInspection = runOnTheFlyInspection;
        }

        public String getRulesPath() {
            return rulesPath;
        }

        public boolean isCheckTests() {
            return checkTests;
        }

        public PmdVersion getPmdVersion() {
            return pmdVersion;
        }

        public JavaVersion getJdkVersion() {
            return jdkVersion;
        }

        public boolean isRunOnTheFlyInspection() {
            return runOnTheFlyInspection;
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }
            final State state = (State) other;
            return checkTests == state.checkTests
                   && runOnTheFlyInspection == state.runOnTheFlyInspection
                   && Objects.equals(rulesPath, state.rulesPath)
                   && Objects.equals(pmdVersion, state.pmdVersion)
                   && Objects.equals(jdkVersion, state.jdkVersion);
        }

        @Override
        public int hashCode() {
            return Objects.hash(rulesPath, checkTests, pmdVersion, jdkVersion);
        }
    }


}
