package com.github.ybroeker.pmdidea.pmd;

import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

import net.sourceforge.pmd.RuleViolation;

public interface PmdRunListener {

    void processFile();

    void start(int nrOfFiles);

    void finished();

    void addViolation(RuleViolation ruleViolation);

}
