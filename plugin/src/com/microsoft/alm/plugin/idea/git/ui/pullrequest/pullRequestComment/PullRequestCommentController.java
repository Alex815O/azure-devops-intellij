package com.microsoft.alm.plugin.idea.git.ui.pullrequest.pullRequestComment;

public class PullRequestCommentController {

    private PullRequestCommentDialog view;
    private PullRequestCommentModel model;

    public PullRequestCommentController() {
        this.model = new PullRequestCommentModel();
        this.view = new PullRequestCommentDialog(this);
    }

    public void show() {
        view.show();
    }

    protected void addComment() {}

    protected void cancelComment() {}
}
