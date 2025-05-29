package com.microsoft.alm.plugin.external.models.pullRequestThread;

import java.sql.Timestamp;
import java.util.List;

public class Comment {
    private Integer id;
    private Short parentCommentId;
    private IdentityRef author;
    private String content;
    private Timestamp publishedDate;
    private Timestamp lastUpdatedDate;
    private Timestamp lastContentUpdatedDate;
    private CommentType commentType;
    private List<IdentityRef> usersLiked;
    private ReferenceLinks _links;

    public Comment() {
    }

    public Comment(String content, Short parentCommentId, CommentType commentType) {
        this.content = content;
        this.parentCommentId = parentCommentId;
        this.commentType = commentType;
    }

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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