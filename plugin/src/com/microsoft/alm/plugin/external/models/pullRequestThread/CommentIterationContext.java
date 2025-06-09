package com.microsoft.alm.plugin.external.models.pullRequestThread;

public class CommentIterationContext {
    private short firstComparingIteration;
    private short secondComparingIteration;

    public short getFirstComparingIteration() {
        return firstComparingIteration;
    }

    public void setFirstComparingIteration(short firstComparingIteration) {
        this.firstComparingIteration = firstComparingIteration;
    }

    public short getSecondComparingIteration() {
        return secondComparingIteration;
    }

    public void setSecondComparingIteration(short secondComparingIteration) {
        this.secondComparingIteration = secondComparingIteration;
    }
}