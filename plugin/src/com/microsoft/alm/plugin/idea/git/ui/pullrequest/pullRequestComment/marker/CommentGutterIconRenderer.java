package com.microsoft.alm.plugin.idea.git.ui.pullrequest.pullRequestComment.marker;

import com.intellij.diff.util.DiffGutterRenderer;
import com.intellij.openapi.util.NlsContexts;
import com.microsoft.alm.plugin.idea.git.ui.pullrequest.pullRequestComment.PullRequestCommentController;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

public class CommentGutterIconRenderer extends DiffGutterRenderer {

    public CommentGutterIconRenderer(@NotNull Icon icon, @Nullable @NlsContexts.Tooltip String tooltip) {
        super(icon, tooltip);
    }

    @Override
    protected void handleMouseClick() {
        var controller = new PullRequestCommentController();
        controller.show();
    }

}
