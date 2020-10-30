package com.github.ybroeker.pmdidea.config;

import java.util.List;

import javax.swing.*;

import com.github.ybroeker.pmdidea.pmd.PmdAdapterLocator;
import com.github.ybroeker.pmdidea.pmd.PmdVersion;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.util.lang.JavaVersion;


public class PmdConfigurationPanel extends JPanel {

    private final PmdConfigurationModel stateBuilder;

    private final JCheckBox scanTestSources = new JCheckBox();


    public PmdConfigurationPanel(final PmdConfigurationModel stateBuilder) {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.stateBuilder = stateBuilder;

        {
            scanTestSources.setText("Scan Test Sources");
            scanTestSources.setSelected(stateBuilder.isCheckTests());
            scanTestSources.addChangeListener(e -> stateBuilder.setCheckTests(scanTestSources.isSelected()));
            this.add(scanTestSources);
            stateBuilder.addPropertyChangeListener("checkTests", evt -> scanTestSources.setSelected(stateBuilder.isCheckTests()));
        }
        {
            final List<PmdVersion> availableVersions = PmdAdapterLocator.getAvailableVersions();
            final ComboBox<PmdVersion> version = new ComboBox<>(availableVersions.toArray(new PmdVersion[0]));
            version.setSelectedItem(stateBuilder.getPmdVersion());
            version.addItemListener(e -> {
                if (e.getItem() != null) {
                    stateBuilder.setPmdVersion((PmdVersion) e.getItem());
                }
            });
            this.add(version);
            stateBuilder.addPropertyChangeListener("pmdVersion", evt -> version.setSelectedItem(stateBuilder.getPmdVersion()));
        }

        {
            final JavaVersion[] javaVersions = JavaVersions.getAllJavaVersions();

            final ComboBox<JavaVersion> jdkVersion = new ComboBox<>(javaVersions);
            jdkVersion.setSelectedItem(stateBuilder.getJdkVersion());
            jdkVersion.addItemListener(e -> {
                if (e.getItem() != null) {
                    stateBuilder.setJdkVersion(((JavaVersion) e.getItem()));
                }
            });
            this.add(jdkVersion);
            stateBuilder.addPropertyChangeListener("jdkVersion", evt -> jdkVersion.setSelectedItem(stateBuilder.getJdkVersion()));

        }
    }


}
