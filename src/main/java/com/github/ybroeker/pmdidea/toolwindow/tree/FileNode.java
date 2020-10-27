package com.github.ybroeker.pmdidea.toolwindow.tree;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.sourceforge.pmd.RulePriority;

public class FileNode implements ViolationsNode {

    private final File file;
    private final List<ViolationNode> ruleViolations;
    private final Set<RulePriority> rulePriorities;

    public FileNode(final String filePath, final List<ViolationNode> ruleViolations) {
        this.file = new File(filePath);
        this.ruleViolations = ruleViolations;
        this.rulePriorities = ruleViolations.stream()
                .map(ViolationsNode::getRulePriorities)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    public String getFileName() {
        return file.getName();
    }

    public String getFilePath() {
        return file.getAbsolutePath();
    }

    @Override
    public int getViolationsCount() {
        return ruleViolations.size();
    }

    @Override
    public Set<RulePriority> getRulePriorities() {
        return rulePriorities;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public List<? extends ViolationsNode> getChildren() {
        return ruleViolations;
    }

    public List<ViolationNode> getRuleViolations() {
        return ruleViolations;
    }

}
