package com.github.ybroeker.pmdidea.actions;

import com.github.ybroeker.pmdidea.Level;
import com.github.ybroeker.pmdidea.LevelFilterModel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractLevelToggle extends ToggleAction  {

    private final Level level;

    protected AbstractLevelToggle(final Level level) {
        this.level = level;
    }

    @Override
    public boolean isSelected(@NotNull final AnActionEvent event) {
        final LevelFilterModel levelFilterModel = getLevelFilterModel(event);
        return levelFilterModel.isVisible(level);
    }

    @Override
    public void setSelected(@NotNull final AnActionEvent event, final boolean state) {
        final LevelFilterModel levelFilterModel = getLevelFilterModel(event);
        levelFilterModel.setVisible(level, state);
    }

    private LevelFilterModel getLevelFilterModel(final @NotNull AnActionEvent event) {
        return event.getProject().getService(LevelFilterModel.class);
    }

}
