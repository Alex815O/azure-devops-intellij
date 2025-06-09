package com.microsoft.alm.plugin.external.models.pullRequestThread;

public class CommentTrackingCriteria {
    private int firstComparingIteration;
    private String origFilePath;
    private CommentPosition origLeftFileEnd;
    private CommentPosition origLeftFileStart;
    private CommentPosition origRightFileEnd;
    private CommentPosition origRightFileStart;
    private int secondComparingIteration;

    public int getFirstComparingIteration() {
        return firstComparingIteration;
    }

    public void setFirstComparingIteration(int firstComparingIteration) {
        this.firstComparingIteration = firstComparingIteration;
    }

    public String getOrigFilePath() {
        return origFilePath;
    }

    public void setOrigFilePath(String origFilePath) {
        this.origFilePath = origFilePath;
    }

    public CommentPosition getOrigLeftFileEnd() {
        return origLeftFileEnd;
    }

    public void setOrigLeftFileEnd(CommentPosition origLeftFileEnd) {
        this.origLeftFileEnd = origLeftFileEnd;
    }

    public CommentPosition getOrigLeftFileStart() {
        return origLeftFileStart;
    }

    public void setOrigLeftFileStart(CommentPosition origLeftFileStart) {
        this.origLeftFileStart = origLeftFileStart;
    }

    public CommentPosition getOrigRightFileEnd() {
        return origRightFileEnd;
    }

    public void setOrigRightFileEnd(CommentPosition origRightFileEnd) {
        this.origRightFileEnd = origRightFileEnd;
    }

    public CommentPosition getOrigRightFileStart() {
        return origRightFileStart;
    }

    public void setOrigRightFileStart(CommentPosition origRightFileStart) {
        this.origRightFileStart = origRightFileStart;
    }

    public int getSecondComparingIteration() {
        return secondComparingIteration;
    }

    public void setSecondComparingIteration(int secondComparingIteration) {
        this.secondComparingIteration = secondComparingIteration;
    }
}