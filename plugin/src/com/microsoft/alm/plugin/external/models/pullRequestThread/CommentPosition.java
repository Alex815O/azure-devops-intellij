package com.microsoft.alm.plugin.external.models.pullRequestThread;

public class CommentPosition {
    private int line;
    private int offset;

    public CommentPosition() {
    }

    public CommentPosition(int line, int offset) {
        this.line = line;
        this.offset = offset;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}