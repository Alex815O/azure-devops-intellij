package com.microsoft.alm.plugin.external.models.pullRequestThread;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class GitPullRequestCommentThread {
    private GitPullRequestCommentThreadContext pullRequestThreadContext;
    private Integer id;
    private Timestamp publishedDate;
    private Timestamp lastUpdatedDate;
    private List<Comment> comments;
    private CommentThreadContext threadContext;
    private PropertiesCollection properties;
    private Map<String, IdentityRef> identities;
    private boolean isDeleted;
    private ReferenceLinks _links;
    private CommentThreadStatus status;


    public GitPullRequestCommentThread createMinimalCommentThread() {
        var thread = new GitPullRequestCommentThread();
        thread.setComments(this.getComments().stream()
                .filter(comment -> comment.getId() == null)
                .map(comment -> new Comment(comment.getContent(), comment.getParentCommentId(), comment.getCommentType()))
                .collect(Collectors.toList()));
        thread.setThreadContext(new CommentThreadContext(this.threadContext.getFilePath(), this.threadContext.getRightFileEnd(), this.threadContext.getRightFileStart()));
        thread.setStatus(this.getStatus());
        return thread;
    }

    public GitPullRequestCommentThreadContext getPullRequestThreadContext() {
        return pullRequestThreadContext;
    }

    public void setPullRequestThreadContext(GitPullRequestCommentThreadContext pullRequestThreadContext) {
        this.pullRequestThreadContext = pullRequestThreadContext;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Timestamp publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Timestamp getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public CommentThreadContext getThreadContext() {
        return threadContext;
    }

    public void setThreadContext(CommentThreadContext threadContext) {
        this.threadContext = threadContext;
    }

    public PropertiesCollection getProperties() {
        return properties;
    }

    public void setProperties(PropertiesCollection properties) {
        this.properties = properties;
    }

    public Map<String, IdentityRef> getIdentities() {
        return identities;
    }

    public void setIdentities(Map<String, IdentityRef> identities) {
        this.identities = identities;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public ReferenceLinks get_links() {
        return _links;
    }

    public void set_links(ReferenceLinks _links) {
        this._links = _links;
    }

    public CommentThreadStatus getStatus() {
        return status;
    }

    public void setStatus(CommentThreadStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GitPullRequestCommentThread thread = (GitPullRequestCommentThread) o;
        return Objects.equals(id, thread.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}