package com.github.ybroeker.pmdidea.logging;

import java.util.logging.*;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;


public class PmdLoggingConfigurator implements StartupActivity {

    private static final Logger PMD_LOGGER = Logger.getLogger("net.sourceforge.pmd");

    @Override
    public void runActivity(@NotNull final Project project) {
        final Logger logger = PMD_LOGGER;
        logger.setUseParentHandlers(false);
        logger.addHandler(new PmdLogHandler());
    }

}
