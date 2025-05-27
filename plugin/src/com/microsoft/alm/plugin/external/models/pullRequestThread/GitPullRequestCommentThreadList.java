package com.microsoft.alm.plugin.external.models.pullRequestThread;

import java.util.List;

public class GitPullRequestCommentThreadList {
    private List<GitPullRequestCommentThread> value;

    public List<GitPullRequestCommentThread> getValue() {
        return value;
    }

    public void setValue(List<GitPullRequestCommentThread> value) {
        this.value = value;
    }
}
