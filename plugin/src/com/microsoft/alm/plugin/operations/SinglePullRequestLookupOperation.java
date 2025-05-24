package com.microsoft.alm.plugin.operations;

import com.microsoft.alm.plugin.context.ServerContext;
import com.microsoft.alm.plugin.context.ServerContextManager;
import com.microsoft.alm.plugin.context.rest.GitHttpClientEx;
import com.microsoft.alm.sourcecontrol.webapi.model.GitChange;
import com.microsoft.alm.sourcecontrol.webapi.model.GitCommitChanges;
import com.microsoft.alm.sourcecontrol.webapi.model.GitCommitRef;
import com.microsoft.alm.sourcecontrol.webapi.model.GitPullRequest;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.NotAuthorizedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class SinglePullRequestLookupOperation extends Operation {
    private static final Logger logger = LoggerFactory.getLogger(PullRequestLookupOperation.class);


    private final String gitRemoteUrl;

    private final SinglePullRequestLookupResults result = new SinglePullRequestLookupResults();

    public static class SinglePullRequestLookupInput extends CredInputsImpl {
        private final int pullRequestId;

        public SinglePullRequestLookupInput(int pullRequestId) {
            this.pullRequestId = pullRequestId;
        }

        public int getPullRequestId() {
            return pullRequestId;
        }
    }

    public class SinglePullRequestLookupResults extends ResultsImpl {
        private GitPullRequest pullRequest;
        private List<GitChange> changes;

        public SinglePullRequestLookupResults() {
        }

        private void init(GitPullRequest pullRequest, List<GitCommitChanges> changes) {
            this.pullRequest = pullRequest;
            this.changes = changes.stream()
                    .flatMap(gitCommitChange ->
                            gitCommitChange.getChanges().stream())
                    .collect(Collectors.toList());
        }

        public GitPullRequest getPullRequest() {
            return pullRequest;
        }

        public List<String> getChangedFiles() {
            return this.changes.stream().map(changes -> changes.getItem().getPath()).collect(Collectors.toList());
        }

        public String getSourceBranchName() {
            return this.pullRequest.getSourceRefName();
        }

        public String getTargetBranchName() {
            return this.pullRequest.getTargetRefName();
        }
    }

    public SinglePullRequestLookupOperation(final String gitRemoteUrl) {
        logger.info("SinglePullRequestLookupOperation created.");
        assert gitRemoteUrl != null;
        this.gitRemoteUrl = gitRemoteUrl;
    }

    public void doWork(final Inputs inputs) {
        logger.info("SinglePullRequestLookupOperation.doWork()");
        onLookupStarted();

        final ServerContext context = doAuthorization(inputs);

        doLookup(context, ((SinglePullRequestLookupInput) inputs).getPullRequestId());

        onLookupResults(this.result);
        onLookupCompleted();
    }

    protected void doLookup(final ServerContext context, final int pullRequestId) {
        try {

            final GitHttpClientEx gitHttpClient = context.getGitHttpClient();
            final GitPullRequest pullRequest = getPullRequest(context, pullRequestId, gitHttpClient);
            var changes = getChangesOfPullRequest(context, pullRequest, gitHttpClient);

            this.result.init(pullRequest, changes);

            logger.debug("doLookup: Found the pull requests {} on repo {}", pullRequest.getPullRequestId(), context.getGitRepository().getRemoteUrl());
        } catch (Throwable t) {
            logger.warn("doLookup: failed with an exception", t);
            terminate(t);
        }
    }

    private static GitPullRequest getPullRequest(ServerContext context, int pullRequestId, GitHttpClientEx gitHttpClient) {
        return gitHttpClient.getPullRequest(
                context.getGitRepository().getId(),
                pullRequestId,
                256,
                0,
                101,
                true
        );
    }

    private static @NotNull List<GitCommitChanges> getChangesOfPullRequest(ServerContext context, GitPullRequest pullRequest, GitHttpClientEx gitHttpClient) {
        return Arrays.stream(pullRequest.getCommits())
                .map(GitCommitRef::getCommitId)
                .map(commitId ->
                        gitHttpClient.getChanges(commitId, context.getGitRepository().getId(), 1, 0))
                .collect(Collectors.toList());
    }

    @Override
    protected void terminate(final Throwable t) {
        super.terminate(t);

        this.result.error = t;
        onLookupResults(this.result);
        onLookupCompleted();
    }

    private ServerContext doAuthorization(final Inputs inputs) {
        ServerContext context = null;
        if (((CredInputsImpl) inputs).getPromptForCreds() == true) {
            final List<ServerContext> authenticatedContexts = new ArrayList<ServerContext>();
            final List<Future> authTasks = new ArrayList<Future>();
            //TODO: get rid of the calls that create more background tasks unless they run in parallel
            try {
                authTasks.add(OperationExecutor.getInstance().submitOperationTask(new Runnable() {
                    @Override
                    public void run() {
                        // Get the authenticated context for the gitRemoteUrl
                        // This should be done on a background thread so as not to block UI or hang the IDE
                        // Get the context before doing the server calls to reduce possibility of using an outdated context with expired credentials
                        final ServerContext context = ServerContextManager.getInstance().getUpdatedContext(gitRemoteUrl, false);
                        if (context != null) {
                            authenticatedContexts.add(context);
                        }
                    }
                }));
                OperationExecutor.getInstance().wait(authTasks);
            } catch (Throwable t) {
                logger.warn("doWork: failed to get authenticated server context", t);
                terminate(new NotAuthorizedException(gitRemoteUrl));
                return null;
            }

            if (authenticatedContexts == null || authenticatedContexts.size() != 1) {
                //no context was found, user might have cancelled
                terminate(new NotAuthorizedException(gitRemoteUrl));
                return null;
            }
            context = authenticatedContexts.get(0);
        } else {
            context = ServerContextManager.getInstance().createContextFromGitRemoteUrl(gitRemoteUrl, false);
            if (context == null) {
                terminate(new NotAuthorizedException(gitRemoteUrl));
                return null;
            }
        }
        return context;
    }

}
