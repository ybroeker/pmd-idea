package com.github.ybroeker.pmdidea;

import java.io.File;
import java.util.*;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.java.JavaSourceRootType;

@Service
public final class FileCollector {

    private final Project project;

    public FileCollector(final Project project) {
        this.project = project;
    }

    public List<File> getFilesToScan(final boolean testSources) {
        final Set<JavaSourceRootType> sourceTypes = getSourceTypes(testSources);

        final VirtualFile[] contentRoots = ProjectRootManager.getInstance(project).getContentSourceRoots();

        final ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();

        final List<File> files = new ArrayList<>();
        for (final VirtualFile selectedFile : contentRoots) {
            VfsUtilCore.visitChildrenRecursively(selectedFile, new VirtualFileVisitor<Void>() {
                @Override
                public boolean visitFile(final @NotNull VirtualFile file) {
                    if (!fileIndex.isUnderSourceRootOfType(file, sourceTypes)) {
                        return false;
                    }

                    if (isExcluded(file)) {
                        return false;
                    }

                    if (isScanableFile(file)) {
                        files.add(new File(file.getPresentableUrl()));
                    }

                    return true;
                }
            });
        }

        return files;
    }

    public List<File> getCurrentFile() {
        final Optional<VirtualFile> selectedFile = getSelectedFile();

        return selectedFile.filter(this::isScanableFile)
                .map(file -> new File(file.getPresentableUrl()))
                .map(Collections::singletonList)
                .orElse(Collections.emptyList());
    }


    private Optional<VirtualFile> getSelectedFile() {
        final Editor selectedTextEditor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (selectedTextEditor == null) {
            return Optional.empty();
        }
        final VirtualFile selectedFile = FileDocumentManager.getInstance().getFile(selectedTextEditor.getDocument());
        if (selectedFile != null) {
            return Optional.of(selectedFile);
        }

        final VirtualFile[] selectedFiles = FileEditorManager.getInstance(project).getSelectedFiles();
        if (selectedFiles.length > 0) {
            return Optional.of(selectedFiles[0]);
        }

        return Optional.empty();
    }

    private Set<JavaSourceRootType> getSourceTypes(final boolean testSources) {
        final Set<JavaSourceRootType> sourceTypes = new HashSet<>(Collections.singleton(JavaSourceRootType.SOURCE));
        if (testSources) {
            sourceTypes.add(JavaSourceRootType.TEST_SOURCE);
        }
        return sourceTypes;
    }

    /**
     * Checks if a file or it's parent is excluded.
     *
     * Check for parent is necessary to exclude generated sources in excluded folders like {@code target/generated-sources}
     *
     * @param file the file to check
     * @return if the file is excluded
     */
    private boolean isExcluded(final VirtualFile file) {
        final ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();

        return fileIndex.isExcluded(file) || fileIndex.isExcluded(file.getParent());
    }

    private boolean isScanableFile(final VirtualFile virtualFile) {
        return !virtualFile.isDirectory() && isScanableFileType(virtualFile);
    }

    private boolean isScanableFileType(final VirtualFile virtualFile) {
        return JavaFileType.INSTANCE.equals(virtualFile.getFileType());
    }

}
