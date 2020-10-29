package com.github.ybroeker.pmdidea.pmd;


public class PmdRule {

    private final String name;

    private final String description;

    private final PmdRulePriority pmdRulePriority;

    public PmdRule(final String name, final String description, final PmdRulePriority pmdRulePriority) {
        this.name = name;
        this.description = description;
        this.pmdRulePriority = pmdRulePriority;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public PmdRulePriority getPmdRulePriority() {
        return pmdRulePriority;
    }
}
