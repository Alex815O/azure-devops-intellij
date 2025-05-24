package com.microsoft.alm.plugin.idea.git.ui.pullrequest.diff;

import com.intellij.openapi.project.Project;
import com.microsoft.alm.plugin.operations.Operation;
import com.microsoft.alm.plugin.operations.SinglePullRequestLookupOperation;
import com.microsoft.alm.sourcecontrol.webapi.model.GitPullRequest;
import git4idea.GitBranch;
import git4idea.repo.GitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DiffViewerModel {

    private static final Logger log = LoggerFactory.getLogger(DiffViewerModel.class);
    private GitBranch sourceBranch;
    private GitBranch targetBranch;
    private GitRepository gitRepository;
    private Project project;
    private final int pullRequestId;
    private GitPullRequest pullRequest;

    private List<String> comments; // TODO: define correct Datatype

    public DiffViewerModel(Project project, GitRepository gitRepository, int pullRequestId) {
        this.gitRepository = gitRepository;
        this.pullRequestId = pullRequestId;
        this.project = project;
    }

    /**
     * Fetches relevant Information from the server.
     */
    public void updatePullRequestInfos() {
        var url = gitRepository.getPresentableUrl();
        final SinglePullRequestLookupOperation activeOperation = new SinglePullRequestLookupOperation(url);

        activeOperation.doWork(getOperationInputs());

        activeOperation.addListener(new SinglePullRequestLookupOperation.Listener() {

            @Override
            public void notifyLookupStarted() { }

            @Override
            public void notifyLookupCompleted() {
                log.info("Pull request lookup completed: {}", pullRequest);
            }

            @Override
            public void notifyLookupResults(Operation.Results results) {
                if (results.getError() != null) {
                    throw new RuntimeException(results.getError());
                }
                pullRequest = ((SinglePullRequestLookupOperation.SinglePullRequestLookupResults) results).getPullRequest();
            }
        });
    }

    public List<String> getComments() {
        return null;
    }

    public Operation.Inputs getOperationInputs() {
        return new SinglePullRequestLookupOperation.SinglePullRequestLookupInput(this.pullRequestId);
    }

    public void cleanup() {
        this.sourceBranch = null;
        this.targetBranch = null;
        this.gitRepository = null;
        this.pullRequest = null;
        this.comments = null;
    }

}
