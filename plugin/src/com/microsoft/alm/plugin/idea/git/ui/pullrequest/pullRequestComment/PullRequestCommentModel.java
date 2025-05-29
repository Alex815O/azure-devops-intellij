package com.microsoft.alm.plugin.idea.git.ui.pullrequest.pullRequestComment;

import com.microsoft.alm.plugin.external.models.pullRequestThread.Comment;
import com.microsoft.alm.plugin.external.models.pullRequestThread.CommentThreadContext;
import com.microsoft.alm.plugin.external.models.pullRequestThread.CommentThreadStatus;
import com.microsoft.alm.plugin.external.models.pullRequestThread.CommentType;
import com.microsoft.alm.plugin.external.models.pullRequestThread.GitPullRequestCommentThread;

import java.util.ArrayList;

public class PullRequestCommentModel {

    private GitPullRequestCommentThread thread;

    public PullRequestCommentModel(CommentThreadContext threadPosition) {
        this.thread = new GitPullRequestCommentThread();
        this.thread.setComments(new ArrayList<>());
        this.thread.setStatus(CommentThreadStatus.active);
        this.thread.setThreadContext(threadPosition);
    }

    public void addComment(String content) {
        var comment = new Comment(content, (short) 0, CommentType.text);
        this.thread.getComments().add(comment);
    }

    public GitPullRequestCommentThread getThread() {
        return thread;
    }
}
