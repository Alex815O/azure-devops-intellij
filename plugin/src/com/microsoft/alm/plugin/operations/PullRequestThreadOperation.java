package com.microsoft.alm.plugin.operations;

import com.microsoft.alm.plugin.context.ServerContext;
import com.microsoft.alm.plugin.context.ServerContextManager;
import com.microsoft.alm.plugin.external.models.pullRequestThread.GitPullRequestCommentThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.ws.rs.NotAuthorizedException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class PullRequestThreadOperation extends Operation {

    private static final Logger log = LoggerFactory.getLogger(PullRequestThreadOperation.class);


    protected final String gitRemoteUrl;
    protected PullRequestThreadOperationResult result;

    public static class PullRequestThreadOperationInput extends Operation.CredInputsImpl {
        protected @Nullable GitPullRequestCommentThread gitPullRequestCommentThread;
        protected int pullRequestId;

        public PullRequestThreadOperationInput(int pullRequestId) {
            super.setPromptForCreds(false);
            this.pullRequestId = pullRequestId;
        }

        public PullRequestThreadOperationInput(int pullRequestId, @Nullable GitPullRequestCommentThread gitPullRequestCommentThread) {
            this.pullRequestId = pullRequestId;
            this.gitPullRequestCommentThread = gitPullRequestCommentThread;
        }
    }

    public class PullRequestThreadOperationResult extends ResultsImpl {
        protected GitPullRequestCommentThread gitPullRequestCommentThread;
        protected List<GitPullRequestCommentThread> gitPullRequestCommentThreads;

        public PullRequestThreadOperationResult(GitPullRequestCommentThread gitPullRequestCommentThread) {
            this.gitPullRequestCommentThread = gitPullRequestCommentThread;
        }

        public PullRequestThreadOperationResult(List<GitPullRequestCommentThread> gitPullRequestCommentThreads) {
            this.gitPullRequestCommentThreads = gitPullRequestCommentThreads;
        }

        public List<GitPullRequestCommentThread> gitPullRequestCommentThreads() {
            return gitPullRequestCommentThreads;
        }
    }

    public PullRequestThreadOperation(String gitRemoteUrl) {
        assert gitRemoteUrl != null;
        this.gitRemoteUrl = gitRemoteUrl;
    }

    public void doWork(final Inputs inputs) {
        log.info("PullRequestThreadOperation.doWork()");
        onLookupStarted();

        final ServerContext context = doAuthorization(inputs);

        doLookup(context, (PullRequestThreadOperationInput) inputs);

        onLookupResults(this.result);
        onLookupCompleted();
    }

    protected void doLookup(final ServerContext context, PullRequestThreadOperationInput inputs) {
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
                log.warn("doWork: failed to get authenticated server context", t);
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
