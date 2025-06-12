package com.microsoft.alm.plugin.idea.git.actions;

import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffDialogHints;
import com.intellij.diff.DiffManager;
import com.intellij.diff.chains.SimpleDiffRequestChain;
import com.intellij.diff.contents.DiffContent;
import com.intellij.diff.requests.DiffRequest;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import com.microsoft.alm.plugin.operations.Operation;
import com.microsoft.alm.plugin.operations.OperationExecutor;
import com.microsoft.alm.plugin.operations.OperationFactory;
import com.microsoft.alm.plugin.operations.SinglePullRequestLookupOperation;
import git4idea.GitUtil;
import git4idea.commands.Git;
import git4idea.commands.GitCommand;
import git4idea.commands.GitLineHandler;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ComparePullRequestAction extends DumbAwareAction {

    public static final DataKey<@Nullable Integer> PULL_REQUEST_ID_DATA_KEY = DataKey.create("pullRequestId");
    public static final Key PULL_REQUEST_ID_KEY = new com.intellij.openapi.util.Key<>("pullRequestId");
    public static final Key PULL_REQUEST_FILE_PATH = new com.intellij.openapi.util.Key<>("filePath");

    private static final Logger log = LoggerFactory.getLogger(ComparePullRequestAction.class);


    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

        var project = anActionEvent.getProject();
        var repository = getGitRepository(project);
        var remoteUrl = extractRemoteURL(repository);
        var pullRequestId = Objects.requireNonNull(anActionEvent.getData(PULL_REQUEST_ID_DATA_KEY));

        var pullRequestLookupOperation = OperationFactory.createSinglePullRequestLookupOperation(remoteUrl);
        pullRequestLookupOperation.addListener(new Operation.Listener() {
            @Override
            public void notifyLookupStarted() {

            }

            @Override
            public void notifyLookupCompleted() {

            }

            @Override
            public void notifyLookupResults(Operation.Results results) {
                if (results.getError() != null) {
                    log.error(results.getError().getMessage());
                    return;
                }
                var pullRequestResult = (SinglePullRequestLookupOperation.SinglePullRequestLookupResults) results;

                var targetBranchRefName = pullRequestResult.getTargetBranchName();
                var sourceBrancheRefName = pullRequestResult.getSourceBranchName();
                var changedFiles = pullRequestResult.getChangedFiles();

                var targetBranch = repository.getBranches().findBranchByName(targetBranchRefName);
                var sourceBranch = repository.getBranches().findBranchByName(sourceBrancheRefName);

                var targetBranchName = targetBranch.getName();
                var sourceBranchName = sourceBranch.getName();

                List<DiffRequest> diffRequests = new ArrayList<>();
                changedFiles.stream()
                        .distinct()
                        .map(changedFile -> repository.getRoot().findFileByRelativePath(changedFile))
                        .map(virtualFile -> VfsUtilCore.getRelativeLocation(virtualFile, repository.getRoot()))
                        .forEach(relativeFilePath -> {
                            var targetFileContent = readFileFromBranch(project, repository, targetBranchName, relativeFilePath);
                            var sourceFileContent = readFileFromBranch(project, repository, sourceBranchName, relativeFilePath);
                            var targetContent = DiffContentFactory.getInstance().create(project, targetFileContent);
                            var sourceContent = DiffContentFactory.getInstance().create(project, sourceFileContent);
                            var request = createDiffRequest(relativeFilePath, targetContent, sourceContent, targetBranchName, sourceBranchName);
                            request.putUserData(PULL_REQUEST_ID_KEY, pullRequestId);
                            request.putUserData(PULL_REQUEST_FILE_PATH, "/" + relativeFilePath);
                            diffRequests.add(request);
                        });
                ApplicationManager.getApplication().invokeLater(() -> {
                    var requestChain = new SimpleDiffRequestChain(diffRequests);
                    DiffManager.getInstance().showDiff(project, requestChain, DiffDialogHints.DEFAULT);
                });
            }
        });

        var operationInput = new SinglePullRequestLookupOperation.SinglePullRequestLookupInput(pullRequestId);
        OperationExecutor.getInstance().executeAsync(pullRequestLookupOperation, operationInput);

    }

    private static @Nullable String extractRemoteURL(GitRepository repository) {
        return repository.getRemotes().stream().findFirst().orElseThrow().getFirstUrl();
    }

    private VirtualFile readFileFromBranch(Project project, GitRepository repository, String branchName, String filePath) {
        GitLineHandler handler = new GitLineHandler(project, repository.getRoot(), GitCommand.SHOW);
        handler.addParameters(String.format("origin/%s:%s", branchName, filePath));

        try {
            String content = Git.getInstance().runCommand(handler).getOutputOrThrow();
            LightVirtualFile virtualFile = new LightVirtualFile(
                    extractFileName(filePath),
                    FileTypeManager.getInstance().getFileTypeByFileName(filePath),
                    content
            );

            virtualFile.setCharset(StandardCharsets.UTF_8);
            virtualFile.setWritable(false);

            return virtualFile;

        } catch (VcsException e) {
            return new LightVirtualFile("error.txt", "File not found: " + e.getMessage());
        }
    }

    private String extractFileName(String path) {
        return path.contains("/") ? path.substring(path.lastIndexOf("/") + 1) : path;
    }

    private @NotNull DiffRequest createDiffRequest(String title, DiffContent contentLeft, DiffContent contentRight, String titleLeft, String titleRight) {
        return new SimpleDiffRequest(
                title,
                contentLeft,
                contentRight,
                titleLeft,
                titleRight
        );
    }

    private @NotNull GitRepository getGitRepository(Project project) {
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
