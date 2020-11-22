package com.github.ybroeker.pmdidea;

import com.github.ybroeker.pmdidea.pmd.PmdAdapterDelegate;
import com.intellij.ide.plugins.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;


public class UnloadListener implements DynamicPluginListener {

    private final Project project;

    public UnloadListener(final Project project) {
        this.project = project;
    }

    @Override
    public void checkUnloadPlugin(@NotNull final IdeaPluginDescriptor pluginDescriptor) throws CannotUnloadPluginException {
        if (pluginDescriptor.getPluginId() == null
            || !pluginDescriptor.getPluginId().getIdString().equals(PmdPlugin.PLUGIN_ID)) {
            return;
        }
        final PmdAdapterDelegate service = project.getService(PmdAdapterDelegate.class);

        if (service.hasLoadedPmd()) {
            throw new CannotUnloadPluginException("Can't unload PMD, restart required");
        }

    }

}
