package com.github.ybroeker.pmdidea.config;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import com.github.ybroeker.pmdidea.pmd.PmdAdapterLocator;
import com.github.ybroeker.pmdidea.pmd.PmdVersion;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.*;
import com.intellij.util.lang.JavaVersion;
import org.jetbrains.annotations.NotNull;


public class PmdConfigurationPanel extends JPanel {

    private final Project project;

    private final JCheckBox scanTestSources;

    private final ComboBox<PmdVersion> versionComboBox;

    private final ComboBox<JavaVersion> jdkComboBox;

    private final TextFieldWithBrowseButton rulesFile;

    public PmdConfigurationPanel(final Project project) {
        super();
        this.project = project;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        PmdConfigurationService.State originalState =  project.getService(PmdConfigurationService.class).getState();

        this.scanTestSources = buildScanTestSources(originalState);
        this.versionComboBox = buildVersionComboBox(originalState);
        this.jdkComboBox = buildJdkComboBox(originalState);
        this.rulesFile = buildRulesField(project, originalState);
    }

    private JCheckBox buildScanTestSources(final PmdConfigurationService.@NotNull State stateBuilder) {
        final JCheckBox scanTestSources = new JCheckBox();
        scanTestSources.setText("Scan Test Sources");
        scanTestSources.setSelected(stateBuilder.isCheckTests());
        this.add(scanTestSources);
        scanTestSources.setAlignmentX(Component.LEFT_ALIGNMENT);

        return scanTestSources;
    }

    private ComboBox<JavaVersion> buildJdkComboBox(final PmdConfigurationService.@NotNull State stateBuilder) {
        final Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JavaVersion[] javaVersions = JavaVersions.getAllJavaVersions();
        final ComboBox<JavaVersion> jdkVersion = new ComboBox<>(javaVersions);
        jdkVersion.setSelectedItem(stateBuilder.getJdkVersion());

        jdkVersion.setMaximumSize(jdkVersion.getPreferredSize());

        horizontalBox.add(jdkVersion);
        horizontalBox.add(new JLabel("Java version to use for scanning"));
        this.add(horizontalBox);

        return jdkVersion;
    }


    private ComboBox<PmdVersion> buildVersionComboBox(final PmdConfigurationService.@NotNull State stateBuilder) {
        final Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        ;
        final List<PmdVersion> availableVersions = PmdAdapterLocator.getAvailableVersions();
        ComboBox<PmdVersion> comboBox = new ComboBox<>(availableVersions.toArray(new PmdVersion[0]));
        comboBox.setSelectedItem(stateBuilder.getPmdVersion());
        comboBox.setMaximumSize(comboBox.getPreferredSize());
        horizontalBox.add(comboBox);
        horizontalBox.add(new JLabel("PMD version to use for scanning"));
        this.add(horizontalBox);


        return comboBox;
    }

    private TextFieldWithBrowseButton buildRulesField(final Project project, final PmdConfigurationService.@NotNull State stateBuilder) {
        final TextFieldWithBrowseButton rulesFile = new TextFieldWithBrowseButton();

        JTextField textField = rulesFile.getTextField();
        textField.setText(stateBuilder.getRulesPath());

        FileChooserDescriptor fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor(StdFileTypes.XML);
        rulesFile.addBrowseFolderListener("PMD-Rules", "Select the PMD rules file", project, fileChooserDescriptor);

        rulesFile.setMaximumSize(new Dimension(rulesFile.getMaximumSize().width, rulesFile.getPreferredSize().height));


        this.add(rulesFile);
        return rulesFile;
    }

    PmdConfigurationService.@NotNull State getNewState() {
        return new PmdConfigurationService.@NotNull State(rulesFile.getText(),
                scanTestSources.isSelected(),
                (PmdVersion) versionComboBox.getSelectedItem(),
                (JavaVersion) jdkComboBox.getSelectedItem()
        );
    }

    public boolean isModified() {
        final PmdConfigurationService service = project.getService(PmdConfigurationService.class);
        return !service.getState().equals(getNewState());
    }

    public void reset() {
        PmdConfigurationService.State originalState =  project.getService(PmdConfigurationService.class).getState();
        rulesFile.setText(originalState.getRulesPath());
        scanTestSources.setSelected(originalState.isCheckTests());
        versionComboBox.setSelectedItem(originalState.getPmdVersion());
        jdkComboBox.setSelectedItem(originalState.getJdkVersion());
    }

    public void apply() {
        final PmdConfigurationService service = project.getService(PmdConfigurationService.class);
        service.setState(getNewState());
    }
}
