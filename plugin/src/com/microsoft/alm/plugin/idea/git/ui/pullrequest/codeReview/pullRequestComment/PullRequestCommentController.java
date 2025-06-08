package com.microsoft.alm.plugin.idea.git.ui.pullrequest.codeReview.pullRequestComment;

import com.microsoft.alm.plugin.external.models.pullRequestThread.Comment;
import com.microsoft.alm.plugin.external.models.pullRequestThread.GitPullRequestCommentThread;

import java.util.List;
import java.util.function.Consumer;

public class PullRequestCommentController {

    private final PullRequestCommentDialog view;
    private final PullRequestCommentModel model;
    private Consumer<GitPullRequestCommentThread> addComment;

    public PullRequestCommentController(GitPullRequestCommentThread thread) {
        this.model = new PullRequestCommentModel(thread);
        this.view = new PullRequestCommentDialog(this);
    }

    public void setAddConsumer(Consumer<GitPullRequestCommentThread> addComment) {
        this.addComment = addComment;
    }

    public void show() {
        view.show();
    }

    protected void addComment(String content) {
        if (content.trim().isEmpty()) {
            return;
        }
        this.model.addComment(content);
        addComment.accept(this.model.getThread());
    }

    protected void cancelComment() {
    }

    public List<Comment> getComments() {
        return this.model.getComments();
    }
}
