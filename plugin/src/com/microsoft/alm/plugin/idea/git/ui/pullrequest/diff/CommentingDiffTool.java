package com.microsoft.alm.plugin.idea.git.ui.pullrequest.diff;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import git4idea.repo.GitRepository;

public class CommentingDiffTool implements Disposable {
    private final DiffViewerModel model;
    private CommentingDiffViewer view;

    public CommentingDiffTool(Project project,
                              GitRepository gitRepository,
                              int pullRequestId) {

//        DiffContentFactory contentFactory = DiffContentFactory.getInstance();
//        DiffContent leftContent = contentFactory.create(project, leftText);
//        DiffContent rightContent = contentFactory.create(project, rightText);
//        super();
        this.model = new DiffViewerModel(project, gitRepository, pullRequestId);
//        this.view = new CommentingDiffViewer(project, this);

//        this.view.showDiff("left text", "right text");
    }

    @Override
    public void dispose() {
        model.cleanup();
        view.dispose();
    }
}