package com.microsoft.alm.plugin.idea.git.extensions;

import com.intellij.diff.DiffContext;
import com.intellij.diff.FrameDiffTool;
import com.intellij.diff.requests.DiffRequest;
import com.microsoft.alm.plugin.idea.git.ui.pullrequest.diff.CommentingDiffViewer;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommentDiffTool implements FrameDiffTool {
    private static final Logger log = LoggerFactory.getLogger(CommentDiffTool.class);

    @Override
    public @NotNull DiffViewer createComponent(@NotNull DiffContext diffContext, @NotNull DiffRequest diffRequest) {
        log.info("Creating comment diff viewer");
        return new CommentingDiffViewer(diffContext, diffRequest);
    }

    @Override
    public @NotNull @Nls(capitalization = Nls.Capitalization.Sentence) String getName() {
        return "Test DIFF";
    }

    @Override
    public boolean canShow(@NotNull DiffContext diffContext, @NotNull DiffRequest diffRequest) {
        return true;
    }
}
