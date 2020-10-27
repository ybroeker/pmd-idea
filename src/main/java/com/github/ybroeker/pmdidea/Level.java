package com.github.ybroeker.pmdidea;

import net.sourceforge.pmd.RulePriority;


public enum Level {

    INFO, WARN, ERROR;

    public boolean matches(final RulePriority rulePriority) {
        return valueOf(rulePriority) == this;
    }

    public static Level valueOf(final RulePriority rulePriority) {
        switch (rulePriority) {
            case LOW:
                return INFO;
            case MEDIUM_LOW:
                return INFO;
            case MEDIUM:
                return WARN;
            case MEDIUM_HIGH:
                return WARN;
            case HIGH:
                return ERROR;
            default:
                throw new AssertionError("Got unknown rulePriority " + rulePriority);
        }
    }

}
