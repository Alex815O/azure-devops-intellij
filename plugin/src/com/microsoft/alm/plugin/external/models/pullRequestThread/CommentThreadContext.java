package com.microsoft.alm.plugin.external.models.pullRequestThread;

public class CommentThreadContext {
    private String filePath;
    private CommentPosition leftFileEnd;
    private CommentPosition leftFileStart;
    private CommentPosition rightFileEnd;
    private CommentPosition rightFileStart;

    public CommentThreadContext() {
    }

    public CommentThreadContext(String filePath, CommentPosition rightFileEnd, CommentPosition rightFileStart) {
        this.filePath = filePath;
        this.rightFileEnd = rightFileEnd;
        this.rightFileStart = rightFileStart;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public CommentPosition getLeftFileEnd() {
        return leftFileEnd;
    }

    public void setLeftFileEnd(CommentPosition leftFileEnd) {
        this.leftFileEnd = leftFileEnd;
    }

    public CommentPosition getLeftFileStart() {
        return leftFileStart;
    }

    public void setLeftFileStart(CommentPosition leftFileStart) {
        this.leftFileStart = leftFileStart;
    }

    public CommentPosition getRightFileEnd() {
        return rightFileEnd;
    }

    public void setRightFileEnd(CommentPosition rightFileEnd) {
        this.rightFileEnd = rightFileEnd;
    }

    public CommentPosition getRightFileStart() {
        return rightFileStart;
    }

    public void setRightFileStart(CommentPosition rightFileStart) {
        this.rightFileStart = rightFileStart;
    }
}