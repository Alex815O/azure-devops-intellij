package com.microsoft.alm.plugin.idea.git.ui.pullrequest.diff;

import com.intellij.diff.DiffContext;
import com.intellij.diff.requests.DiffRequest;
import com.microsoft.alm.plugin.external.models.pullRequestThread.GitPullRequestCommentThread;
import com.microsoft.alm.plugin.idea.common.utils.VcsHelper;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.microsoft.alm.plugin.idea.git.actions.ComparePullRequestAction.PULL_REQUEST_ID_KEY;

public class CommentingDiffModel {


    private final DiffContext diffContext;
    private final DiffRequest diffRequest;
    private List<GitPullRequestCommentThread> threads; // TODO: define correct Datatype

    public CommentingDiffModel(DiffContext context, DiffRequest request) {
        this.diffContext = context;
        this.diffRequest = request;
    }

    public String getRemoteUrl() {
        var project = diffContext.getProject();
        var repository = VcsHelper.getGitRepository(project);

        return repository.getRemotes().stream().findFirst().orElseThrow().getFirstUrl();
    }

    public int getPullRequestId() {
        return (Integer) Objects.requireNonNull(diffRequest.getUserData(PULL_REQUEST_ID_KEY));
    }

    public Map<Integer, GitPullRequestCommentThread> getThreadsPerLineOfFile(String fileName) {

        return this.threads.stream()
                .filter(thread -> thread.getThreadContext() != null)
                .filter(thread -> thread.getThreadContext().getFilePath().contains(fileName))
                .collect(Collectors.toMap(
                        t1 -> t1.getThreadContext().getRightFileStart().getLine(),
                        t1 -> t1
                ));
    }

    public void setThreads(List<GitPullRequestCommentThread> threads) {
        this.threads = threads;
    }

    public String getActiveRequestName() {
        return diffRequest.getTitle();
    }
}

