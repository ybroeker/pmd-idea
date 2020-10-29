package com.github.ybroeker.pmdidea.toolwindow.tree;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.ybroeker.pmdidea.pmd.PmdRulePriority;
import net.sourceforge.pmd.RulePriority;

class RootNode implements ViolationsNode {

    private final int violationsCount;
    private final List<FileNode> fileViolations;
    private final Set<PmdRulePriority> rulePriorities;

    public RootNode(final List<FileNode> fileViolations) {
        this.fileViolations = fileViolations;
        this.violationsCount = fileViolations.stream().mapToInt(FileNode::getViolationsCount).sum();
        this.rulePriorities = fileViolations.stream()
                .map(ViolationsNode::getRulePriorities)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public int getViolationsCount() {
        return violationsCount;
    }

    @Override
    public Set<PmdRulePriority> getRulePriorities() {
        return rulePriorities;
    }

    @Override
    public List<? extends ViolationsNode> getChildren() {
        return fileViolations;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }


    public List<FileNode> getFileViolations() {
        return fileViolations;
    }
}
