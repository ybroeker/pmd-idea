package com.github.ybroeker.pmdidea.pmd;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

public class PmdRuleViolation {

    private final PmdRule pmdRule;

    private final File file;

    private final String message;

    private final PmdViolationPosition position;

    public PmdRuleViolation(final PmdRule pmdRule, final File file, final String message, final PmdViolationPosition position) {
        this.pmdRule = pmdRule;
        this.file = file;
        this.message = message;
        this.position = position;
    }

    public PmdRule getPmdRule() {
        return pmdRule;
    }

    public File getFile() {
        return file;
    }

    public String getMessage() {
        return message;
    }

    public PmdViolationPosition getPosition() {
        return position;
    }
}
