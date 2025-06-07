package com.microsoft.alm.plugin.idea.git.ui.pullrequest.diff;

import com.intellij.diff.DiffContext;
import com.intellij.diff.requests.DiffRequest;
import com.microsoft.alm.plugin.external.models.pullRequestThread.GitPullRequestCommentThread;
import com.microsoft.alm.plugin.idea.common.utils.VcsHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.microsoft.alm.plugin.idea.git.actions.ComparePullRequestAction.PULL_REQUEST_FILE_PATH;
import static com.microsoft.alm.plugin.idea.git.actions.ComparePullRequestAction.PULL_REQUEST_ID_KEY;

public class CommentingDiffModel {


    private final DiffContext diffContext;
    private final DiffRequest diffRequest;
    private List<GitPullRequestCommentThread> threads = new ArrayList<>();
    private List<GitPullRequestCommentThread> newThreads = new ArrayList<>();


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

    public String getOpenFilePath() {
        return diffRequest.getUserData(PULL_REQUEST_FILE_PATH).toString();
    }

    public Map<Integer, GitPullRequestCommentThread> getThreadsPerLineOfFile(String fileName) {
        return this.getAllThreads().stream()
                .filter(thread -> thread.getThreadContext() != null)
                .filter(thread -> thread.getThreadContext().getFilePath().contains(fileName))
                .filter(thread -> !thread.isDeleted())
                .filter(this::filterForEmptyThread)
                .collect(Collectors.toMap(
                        t1 -> t1.getThreadContext().getRightFileStart().getLine() - 1,
                        t1 -> t1
                ));
    }

    public List<GitPullRequestCommentThread> getAllThreads() {
        var allThreads = new ArrayList<GitPullRequestCommentThread>();
        allThreads.addAll(this.threads);
        allThreads.addAll(this.newThreads);
        return allThreads;
    }

    public void setThreads(List<GitPullRequestCommentThread> threads) {
        this.threads = threads;
    }

    public void addNewThread(GitPullRequestCommentThread thread) {
        this.threads.add(thread);
    }

    public void updateThreadsComments(GitPullRequestCommentThread thread) {
        var threadToUpdate = this.threads.stream()
                .filter(threadOld -> threadOld.equals(thread))
                .findFirst()
                .orElseThrow();
        var updatedCommentList = threadToUpdate.getComments()
                .stream()
                .filter(c -> Objects.nonNull(c.getId()))
                .collect(Collectors.toList());
        updatedCommentList.addAll(thread.getComments());
        threadToUpdate.setComments(updatedCommentList);
    }

    private boolean filterForEmptyThread(GitPullRequestCommentThread thread) {
        return thread.getComments().stream().anyMatch(comment -> comment.getContent() != null && !comment.getContent().isBlank());
    }

}

