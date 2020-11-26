package com.github.ybroeker.pmdidea.quickfix;

import java.util.HashMap;

import javax.swing.*;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.refactoring.actions.RenameElementAction;
import com.intellij.refactoring.rename.RenameHandlerRegistry;
import icons.SpellcheckerIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RenameToFix extends AbstractLocalQuickFix<PsiNameIdentifierOwner> implements Iconable {

    private final String name;

    public RenameToFix(final String name) {
        this.name = name;
    }

    @Nullable
    protected Editor getEditor(final @NotNull PsiElement element, @NotNull final Project project) {
        return FileEditorManager.getInstance(project).getSelectedTextEditor();
    }

    @Override
    protected void applyFix(final @NotNull Project project, final @NotNull PsiNameIdentifierOwner psiElement) {
        final Editor editor = getEditor(psiElement, project);
        if (editor == null) {
            return;
        }

        final HashMap<String, Object> map = new HashMap<>();
        map.put(CommonDataKeys.EDITOR.getName(), editor);
        map.put(CommonDataKeys.PSI_ELEMENT.getName(), psiElement);

        final DataContext dataContext = SimpleDataContext.getSimpleContext(map, DataManager.getInstance().getDataContext(editor.getComponent()));
        final RenameElementAction action = new RenameElementAction();
        final AnActionEvent event = AnActionEvent.createFromAnAction(action, null, "", dataContext);

        ApplicationManager.getApplication().invokeLater(() -> {
            final Boolean selectAll = editor.getUserData(RenameHandlerRegistry.SELECT_ALL);
            try {
                editor.putUserData(RenameHandlerRegistry.SELECT_ALL, true);
                action.actionPerformed(event);
            } finally {
                editor.putUserData(RenameHandlerRegistry.SELECT_ALL, selectAll);
            }
        });
    }

    @Override
    public @NotNull String getName() {
        return "Rename '" + name + "'";
    }

    @Override
    public @NotNull String getFamilyName() {
        return "Rename ...";
    }

    @Override
    public Icon getIcon(final int flags) {
        return SpellcheckerIcons.Spellcheck;
    }
}
