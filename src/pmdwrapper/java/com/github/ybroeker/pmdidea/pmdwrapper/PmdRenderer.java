package com.github.ybroeker.pmdidea.pmdwrapper;

import java.io.File;
import java.util.Iterator;

import com.github.ybroeker.pmdidea.pmd.*;
import net.sourceforge.pmd.*;
import net.sourceforge.pmd.renderers.AbstractIncrementingRenderer;
import net.sourceforge.pmd.util.datasource.DataSource;
import org.jetbrains.annotations.NotNull;

public class PmdRenderer extends AbstractIncrementingRenderer {

    private final PmdRunListener pmdRunListener;

    public PmdRenderer(final PmdRunListener pmdRunListener) {
        super("pmd-idea", "PMD plugin renderer");
        this.pmdRunListener = pmdRunListener;
        setWriter(new NullWriter());
    }

    @Override
    public String defaultFileExtension() {
        return "txt";
    }

    @Override
    public void start() {
    }

    @Override
    public void end() {
        pmdRunListener.finished();
    }

    @Override
    public void startFileAnalysis(final DataSource dataSource) {
        super.startFileAnalysis(dataSource);
        pmdRunListener.processFile();
    }

    @Override
    public void renderFileViolations(final Iterator<RuleViolation> violations) {
        for (final RuleViolation ruleViolation : ((Iterable<RuleViolation>) () -> violations)) {
            PmdRule rule = map(ruleViolation.getRule());
            PmdRuleViolation violation = new PmdRuleViolation(rule,
                    new File(ruleViolation.getFilename()),
                    rule.getDescription(),
                    getPosition(ruleViolation));
            pmdRunListener.addViolation(violation);
        }
    }

    private PmdViolationPosition getPosition(final RuleViolation ruleViolation) {
        return new PmdViolationPosition(ruleViolation.getBeginLine(),
                ruleViolation.getBeginColumn(),
                ruleViolation.getEndLine(),
                ruleViolation.getEndColumn());
    }

    private PmdRule map(final Rule rule) {
        return new PmdRule(rule.getName(), rule.getDescription(), map(rule.getPriority()));
    }

    private PmdRulePriority map(final RulePriority priority) {
        switch (priority) {
            case LOW:
                return PmdRulePriority.LOW;
            case MEDIUM_LOW:
                return PmdRulePriority.MEDIUM_LOW;
            case MEDIUM:
                return PmdRulePriority.MEDIUM;
            case MEDIUM_HIGH:
                return PmdRulePriority.MEDIUM_HIGH;
            case HIGH:
                return PmdRulePriority.HIGH;
            default:
                throw new AssertionError("Got unknown priority " + priority);
        }
    }

}
