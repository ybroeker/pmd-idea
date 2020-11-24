package com.github.ybroeker.pmdidea.quickfix;

import com.intellij.codeInsight.daemon.impl.actions.SuppressFix;
import com.intellij.codeInsight.intention.LowPriorityAction;
import org.jetbrains.annotations.NotNull;

public class SuppressPmdRuleFix extends SuppressFix implements LowPriorityAction {

    private final String ruleName;

    public SuppressPmdRuleFix(@NotNull final String ruleName) {
        super("PMD." + ruleName);
        this.ruleName = ruleName;
    }

    @Override
    public @NotNull String getText() {
        return "Suppress " + ruleName;
    }

}
