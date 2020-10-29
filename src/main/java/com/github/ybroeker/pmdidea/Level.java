package com.github.ybroeker.pmdidea;

import java.util.EnumMap;
import java.util.Map;

import com.github.ybroeker.pmdidea.pmd.PmdRulePriority;

/**
 * Maps PMDs five priorities to three levels.
 */
public enum Level {

    INFO, WARN, ERROR;

    private static final Map<PmdRulePriority, Level> LEVELS = createLevelMap();

    public boolean matches(final PmdRulePriority rulePriority) {
        return valueOf(rulePriority) == this;
    }

    public static Level valueOf(final PmdRulePriority rulePriority) {
        final Level level = LEVELS.get(rulePriority);
        if (level == null) {
            throw new AssertionError("Got unknown rulePriority " + rulePriority);
        }
        return level;
    }

    private static Map<PmdRulePriority, Level> createLevelMap() {
        EnumMap<PmdRulePriority, Level> map = new EnumMap<>(PmdRulePriority.class);
        map.put(PmdRulePriority.LOW, INFO);
        map.put(PmdRulePriority.MEDIUM_LOW, INFO);
        map.put(PmdRulePriority.MEDIUM, WARN);
        map.put(PmdRulePriority.MEDIUM_HIGH, WARN);
        map.put(PmdRulePriority.HIGH, ERROR);
        return map;
    }

}
