package com.microsoft.alm.plugin.external.models.pullRequestThread;

import java.sql.Timestamp;
import java.util.List;

public class Comment {
    private ReferenceLinks _links;
    private IdentityRef author;
    private CommentType commentType;
    private String content;
    private short id;
    private boolean isDeleted;
    private Timestamp lastContentUpdatedDate;
    private Timestamp lastUpdatedDate;
    private Short parentCommentId;
    private Timestamp publishedDate;
    private List<IdentityRef> usersLiked;

    public ReferenceLinks get_links() {
        return _links;
    }

    public void set_links(ReferenceLinks _links) {
        this._links = _links;
    }

    public IdentityRef getAuthor() {
        return author;
    }

    public void setAuthor(IdentityRef author) {
        this.author = author;
    }

    public CommentType getCommentType() {
        return commentType;
    }

    public void setCommentType(CommentType commentType) {
        this.commentType = commentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Timestamp getLastContentUpdatedDate() {
        return lastContentUpdatedDate;
    }

    public void setLastContentUpdatedDate(Timestamp lastContentUpdatedDate) {
        this.lastContentUpdatedDate = lastContentUpdatedDate;
    }

    public Timestamp getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Short getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Short parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public Timestamp getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Timestamp publishedDate) {
        this.publishedDate = publishedDate;
    }

    public List<IdentityRef> getUsersLiked() {
        return usersLiked;
    }

    public void setUsersLiked(List<IdentityRef> usersLiked) {
        this.usersLiked = usersLiked;
    }
}