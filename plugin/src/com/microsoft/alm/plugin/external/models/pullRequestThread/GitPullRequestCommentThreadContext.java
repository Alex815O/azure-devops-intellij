package com.microsoft.alm.plugin.external.models.pullRequestThread;

public class GitPullRequestCommentThreadContext {
    private int changeTrackingId;
    private CommentIterationContext iterationContext;
    private CommentTrackingCriteria trackingCriteria;

    public int getChangeTrackingId() {
        return changeTrackingId;
    }

    public void setChangeTrackingId(int changeTrackingId) {
        this.changeTrackingId = changeTrackingId;
    }

    public CommentIterationContext getIterationContext() {
        return iterationContext;
    }

    public void setIterationContext(CommentIterationContext iterationContext) {
        this.iterationContext = iterationContext;
    }

    public CommentTrackingCriteria getTrackingCriteria() {
        return trackingCriteria;
    }

    public void setTrackingCriteria(CommentTrackingCriteria trackingCriteria) {
        this.trackingCriteria = trackingCriteria;
    }
}