package com.microsoft.alm.plugin.operations;

import com.microsoft.alm.plugin.context.ServerContext;

public class PullRequestThreadUpdateOperation extends PullRequestThreadOperation {
    public PullRequestThreadUpdateOperation(String gitRemoteUrl) {
        super(gitRemoteUrl);
    }

    @Override
    protected void doLookup(ServerContext context, PullRequestThreadOperationInput pullRequestThreadOperationInput) {

    }
}
