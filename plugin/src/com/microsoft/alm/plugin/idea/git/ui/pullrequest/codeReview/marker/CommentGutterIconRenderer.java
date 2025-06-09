package com.microsoft.alm.plugin.idea.git.ui.pullrequest.codeReview.marker;

import com.intellij.diff.util.DiffGutterRenderer;
import com.intellij.openapi.util.NlsContexts;
import com.microsoft.alm.plugin.external.models.pullRequestThread.GitPullRequestCommentThread;
import com.microsoft.alm.plugin.idea.git.ui.pullrequest.codeReview.CommentingDiffViewerController;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

public class CommentGutterIconRenderer extends DiffGutterRenderer {

    private final GitPullRequestCommentThread thread;
    private final CommentingDiffViewerController diffController;

    public CommentGutterIconRenderer(@NotNull Icon icon, @Nullable @NlsContexts.Tooltip String tooltip, GitPullRequestCommentThread thread, CommentingDiffViewerController diffController) {
        super(icon, tooltip);
        this.thread = thread;
        this.diffController = diffController;
    }

    @Override
    protected void handleMouseClick() {
        diffController.showCommentThread(thread);
    }

}
