package com.github.ybroeker.pmdidea.pmd;


public class PmdViolationPosition {

    private final int beginLine;
    private final int beginColumn;

    private final int endLine;
    private final int endColumn;

    public PmdViolationPosition(final int beginLine, final int beginColumn, final int endLine, final int endColumn) {
        this.beginLine = beginLine;
        this.beginColumn = beginColumn;
        this.endLine = endLine;
        this.endColumn = endColumn;
    }

    public int getBeginLine() {
        return beginLine;
    }

    public int getBeginColumn() {
        return beginColumn;
    }

    public int getEndLine() {
        return endLine;
    }

    public int getEndColumn() {
        return endColumn;
    }
}
