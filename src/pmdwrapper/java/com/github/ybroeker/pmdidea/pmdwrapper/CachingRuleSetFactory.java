package com.github.ybroeker.pmdidea.pmdwrapper;

import net.sourceforge.pmd.*;
import net.sourceforge.pmd.util.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CachingRuleSetFactory extends RuleSetFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(CachingRuleSetFactory.class);

    private final int ruleSetHash;

    private RuleSets cached;

    public CachingRuleSetFactory(final ResourceLoader resourceLoader, final int ruleSetHash) {
        this(resourceLoader, RulePriority.LOW, true, true, ruleSetHash);
    }

    public CachingRuleSetFactory(final ResourceLoader resourceLoader, final RulePriority minimumPriority, final boolean warnDeprecated, final boolean enableCompatibility, final int ruleSetHash) {
        super(resourceLoader, minimumPriority, warnDeprecated, enableCompatibility);
        this.ruleSetHash = ruleSetHash;
    }

    @Override
    public RuleSets createRuleSets(final String referenceString) throws RuleSetNotFoundException {
        if (cached == null) {
            LOGGER.trace("create new rule set: {}", referenceString);
            cached = super.createRuleSets(referenceString);
        } else {
            LOGGER.trace("use cached rule set: {}", referenceString);
        }
        return cached;
    }

    public boolean hasHash(final int rulesHash) {
        return this.ruleSetHash == rulesHash;
    }

    @Override
    public String toString() {
        return "CachingRuleSetFactory{"
               + "ruleSetHash=" + ruleSetHash
               + '}';
    }
}
