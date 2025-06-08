package com.microsoft.alm.plugin.idea.git.ui.pullrequest.codeReview.pullRequestComment;

import com.microsoft.alm.plugin.external.models.pullRequestThread.Comment;
import com.microsoft.alm.plugin.external.models.pullRequestThread.CommentType;
import com.microsoft.alm.plugin.external.models.pullRequestThread.GitPullRequestCommentThread;

import java.util.List;

public class PullRequestCommentModel {

    private GitPullRequestCommentThread thread;

    public PullRequestCommentModel(GitPullRequestCommentThread thread) {
        this.thread = thread;
    }

    public void addComment(String content) {
        var comment = new Comment(content, (short) 0, CommentType.text);
        this.thread.getComments().add(comment);
    }

    public GitPullRequestCommentThread getThread() {
        return thread;
    }

    public List<Comment> getComments() {
        return thread.getComments();
    }
}
