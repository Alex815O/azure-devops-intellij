package com.microsoft.alm.plugin.idea.git.ui.pullrequest.pullRequestComment;

import com.microsoft.alm.plugin.external.models.pullRequestThread.CommentThreadContext;
import com.microsoft.alm.plugin.external.models.pullRequestThread.GitPullRequestCommentThread;

import java.util.function.Consumer;

public class PullRequestCommentController {

    private PullRequestCommentDialog view;
    private PullRequestCommentModel model;
    private Consumer<GitPullRequestCommentThread> addComment;

    public PullRequestCommentController(CommentThreadContext threadPosition) {
        this.model = new PullRequestCommentModel(threadPosition);
        this.view = new PullRequestCommentDialog(this);
    }

    public void setAddConsumer(Consumer<GitPullRequestCommentThread> addComment) {
        this.addComment = addComment;
    }

    public PullRequestCommentController() {
    }

    public void show() {
        view.show();
    }

    protected void addComment(String content) {
        this.model.addComment(content);
        addComment.accept(this.model.getThread());
    }

    protected void cancelComment() {
    }
}
