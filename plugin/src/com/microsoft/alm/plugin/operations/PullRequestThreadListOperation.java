package com.microsoft.alm.plugin.operations;

import com.microsoft.alm.plugin.context.ServerContext;
import com.microsoft.alm.plugin.context.rest.UrlUtils;
import com.microsoft.alm.plugin.context.rest.VstsHttpClient;
import com.microsoft.alm.plugin.external.models.pullRequestThread.GitPullRequestCommentThread;
import com.microsoft.alm.plugin.external.models.pullRequestThread.GitPullRequestCommentThreadList;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.client.Client;
import java.util.List;
import java.util.Map;

public class PullRequestThreadListOperation extends PullRequestThreadOperation {

    private static final String REST_PATH_LIST_PULL_REQUEST_THREAD = "/_apis/git/repositories/{repositoryId}/pullRequests/{pullRequestId}/threads";

    public PullRequestThreadListOperation(String gitRemoteUrl) {
        super(gitRemoteUrl);
    }

    @Override
    protected void doLookup(ServerContext context, PullRequestThreadOperationInput inputs) {
        var pullRequestId = Integer.toString(inputs.pullRequestId);
        var repositoryId = context.getGitRepository().getId().toString();

        var uri = context.getTeamProjectURI().toString().concat(REST_PATH_LIST_PULL_REQUEST_THREAD);
        uri = enhanceUri(uri, repositoryId, pullRequestId);

        var client = context.getClient();

        var createdGitPullRequestCommandThread = loadCommentThreads(client, uri);
        super.result = new PullRequestThreadOperationResult(createdGitPullRequestCommandThread);
    }

    private static @NotNull List<GitPullRequestCommentThread> loadCommentThreads(Client client, String uri) {
        var commentThreads = VstsHttpClient.sendRequest(client, uri, GitPullRequestCommentThreadList.class);
        return commentThreads.getValue();
    }

    private static String enhanceUri(String uri, String repositoryId, String pullRequestId) {
        String enhancedUri;
        enhancedUri = replacePathParameter(uri, repositoryId, pullRequestId);
        enhancedUri = addParameter(enhancedUri);
        return enhancedUri;
    }

    private static String replacePathParameter(String uri, String repositoryId, String pullRequestId) {
        uri = UrlUtils.replacePathParameters(uri, Map.of(
                "pullRequestId", pullRequestId,
                "repositoryId", repositoryId
        ));
        return uri;
    }

    private static String addParameter(String uri) {
        uri = UrlUtils.addParameters(uri, Map.of(
                "api-version", "7.1"
        ));
        return uri;
    }
}
