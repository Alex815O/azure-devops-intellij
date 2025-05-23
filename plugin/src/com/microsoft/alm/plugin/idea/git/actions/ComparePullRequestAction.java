package com.microsoft.alm.plugin.idea.git.actions;

import com.intellij.diff.DiffManager;
import com.intellij.diff.chains.DiffRequestChain;
import com.intellij.diff.chains.SimpleDiffRequestChain;
import com.intellij.diff.contents.DiffContent;
import com.intellij.diff.contents.EmptyContent;
import com.intellij.diff.requests.DiffRequest;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.diff.util.DiffUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.microsoft.alm.plugin.idea.git.ui.pullrequest.diff.CommentingDiffTool;
import git4idea.GitUtil;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class ComparePullRequestAction extends DumbAwareAction {

    private static final Logger log = LoggerFactory.getLogger(ComparePullRequestAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

        var project = anActionEvent.getProject();
        var repository = getGitRepository(project);


        DiffContent contentLeft = new EmptyContent();
        DiffContent contentRight = new EmptyContent();
        DiffRequest request = new SimpleDiffRequest(
                "Test Heading",
                contentLeft,
                contentRight,
                "Left content",
                "Right content"
        );

        DiffManager.getInstance().showDiff(project, request);
    }

    private @Nullable GitRepository getGitRepository(Project project) {
        var gitRepositories = GitUtil.getRepositories(project);
        return getFirstAndLogOthers(gitRepositories);
    }

    private GitRepository getFirstAndLogOthers(Collection<GitRepository> repositories) {
        if (repositories.isEmpty()) {
            log.error("There are no repositories");
        }
        if (!repositories.isEmpty()) {
            log.info("There are " + repositories.size() + " repositories. The first one is selected");
        }
        return repositories.iterator().next();
    }
}
