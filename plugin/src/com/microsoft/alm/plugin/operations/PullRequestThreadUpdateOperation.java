package com.microsoft.alm.plugin.operations;

import com.microsoft.alm.plugin.context.ServerContext;
import com.microsoft.alm.plugin.context.rest.UrlUtils;
import com.microsoft.alm.plugin.context.rest.VstsHttpClient;
import com.microsoft.alm.plugin.external.models.pullRequestThread.GitPullRequestCommentThread;

import java.util.Map;

public class PullRequestThreadUpdateOperation extends PullRequestThreadOperation {

    private static final String REST_PATH_UPDATE_PULL_REQUEST_THREAD = "/_apis/git/repositories/{repositoryId}/pullRequests/{pullRequestId}/threads/{threadId}";

    public PullRequestThreadUpdateOperation(String gitRemoteUrl) {
        super(gitRemoteUrl);
    }

    @Override
    protected void doLookup(ServerContext context, PullRequestThreadOperationInput inputs) {

        var gitPullRequestCommentThread = inputs.gitPullRequestCommentThread.createMinimalCommentThread();
        var pullRequestId = Integer.toString(inputs.pullRequestId);
        var repositoryId = context.getGitRepository().getId().toString();
        var threadId = Integer.toString(inputs.threadId);

        var uri = context.getTeamProjectURI().toString().concat(REST_PATH_UPDATE_PULL_REQUEST_THREAD);
        uri = enhanceUri(uri, repositoryId, pullRequestId, threadId);

        var client =  context.getClient();

        var createdGitPullRequestCommandThread = VstsHttpClient.sendPATCHRequest(client, uri, gitPullRequestCommentThread, GitPullRequestCommentThread.class);
        super.result = new PullRequestThreadOperationResult(createdGitPullRequestCommandThread);
    }

    private static String enhanceUri(String uri, String repositoryId, String pullRequestId, String threadId) {
        String enhancedUri;
        enhancedUri = replacePathParameter(uri, repositoryId, pullRequestId, threadId);
        enhancedUri = addParameter(enhancedUri);
        return enhancedUri;
    }

    private static String addParameter(String uri) {
        uri = UrlUtils.addParameters(uri, Map.of(
                "api-version", "7.1"
        ));
        return uri;
    }

    private static String replacePathParameter(String uri, String repositoryId, String pullRequestId, String threadId) {
        uri = UrlUtils.replacePathParameters(uri, Map.of(
                "repositoryId", repositoryId,
                "pullRequestId", pullRequestId,
                "threadId", threadId
        ));
        return uri;
    }
}
