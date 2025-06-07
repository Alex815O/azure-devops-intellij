package com.microsoft.alm.plugin.idea.git.ui.pullrequest.diff;

import com.intellij.diff.DiffContext;
import com.intellij.diff.requests.DiffRequest;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.microsoft.alm.plugin.external.models.pullRequestThread.CommentPosition;
import com.microsoft.alm.plugin.external.models.pullRequestThread.CommentThreadContext;
import com.microsoft.alm.plugin.external.models.pullRequestThread.CommentThreadStatus;
import com.microsoft.alm.plugin.external.models.pullRequestThread.GitPullRequestCommentThread;
import com.microsoft.alm.plugin.idea.common.resources.Icons;
import com.microsoft.alm.plugin.idea.git.ui.pullrequest.pullRequestComment.PullRequestCommentController;
import com.microsoft.alm.plugin.idea.git.ui.pullrequest.pullRequestComment.marker.CommentGutterIconRenderer;
import com.microsoft.alm.plugin.operations.Operation;
import com.microsoft.alm.plugin.operations.OperationExecutor;
import com.microsoft.alm.plugin.operations.OperationFactory;
import com.microsoft.alm.plugin.operations.PullRequestThreadOperation;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;
import java.util.ArrayList;

public class CommentingDiffController implements Disposable {
    private final CommentingDiffModel model;
    private CommentingDiffViewer view;

    public CommentingDiffController(@NotNull DiffContext context, @NotNull DiffRequest request) {
        this.model = new CommentingDiffModel(context, request);
        this.view = new CommentingDiffViewer(context, request, this);
    }

    public void loadModel() {
        String remoteUrl = model.getRemoteUrl();
        int pullRequestId = model.getPullRequestId();

        var commentThreadListOperation = OperationFactory.createPullRequestThreadListOperation(remoteUrl);
        var commentThreadListOperationInput = new PullRequestThreadOperation.PullRequestThreadOperationInput(pullRequestId);
        commentThreadListOperation.addListener(new Operation.Listener() {

            @Override
            public void notifyLookupStarted() {

            }

            @Override
            public void notifyLookupCompleted() {

            }

            @Override
            public void notifyLookupResults(Operation.Results results) {
                PullRequestThreadOperation.PullRequestThreadOperationResult result = (PullRequestThreadOperation.PullRequestThreadOperationResult) results;
                model.setThreads(result.gitPullRequestCommentThreads());

                var activeFile = view.getRequest().getTitle();
                var threadsPerLine = model.getThreadsPerLineOfFile(activeFile);
                var editor = view.getEditor();

                threadsPerLine.forEach((line, thread) -> addGutterIcon(editor, line, thread));
            }
        });
        OperationExecutor.getInstance().executeAsync(commentThreadListOperation, commentThreadListOperationInput);
    }

    public CommentingDiffViewer getView() {
        return view;
    }

    protected void createThreadAndShowDialog(EditorMouseEvent event, Editor editor) {
        int line = editor.xyToLogicalPosition(event.getMouseEvent().getPoint()).line;
        int lineOffset = editor.getDocument().getLineEndOffset(line);

        var thread = createThreadObject(this.model.getOpenFilePath(), line+1, lineOffset);

        var commentController = new PullRequestCommentController(thread);
        commentController.setAddConsumer((commentThread) -> {
            this.createThreadOnServerAndModel(commentThread, new Operation.Listener() {
                @Override
                public void notifyLookupResults(Operation.Results results) {
                    var thread = ((PullRequestThreadOperation.PullRequestThreadOperationResult) results).getGitPullRequestCommentThread();
                    model.addNewThread(thread);
                    addGutterIcon(editor, line, thread);
                }

                @Override
                public void notifyLookupCompleted() {

                }

                @Override
                public void notifyLookupStarted() {
                }
            });
        });
        commentController.show();
    }

    public void showCommentThread(GitPullRequestCommentThread thread) {
        var commentController = new PullRequestCommentController(thread);
        commentController.setAddConsumer(this::updateThreadOnServerAndModel);
        commentController.show();
    }

    private @NotNull GitPullRequestCommentThread createThreadObject(String filePath, int line, int lineEndOffset) {
        var threadPosition = new CommentThreadContext(
                filePath,
                new CommentPosition(line, lineEndOffset),
                new CommentPosition(line, 1)
        );
        var thread = new GitPullRequestCommentThread();
        thread.setComments(new ArrayList<>());
        thread.setStatus(CommentThreadStatus.active);
        thread.setThreadContext(threadPosition);
        return thread;
    }

    private void addGutterIcon(Editor editor, int line, GitPullRequestCommentThread threadBehindIcon) {
        Document document = editor.getDocument();
        MarkupModel markupModel = editor.getMarkupModel();

        Icon commentIcon = Icons.PR_COMMENT;
        String toolTipp = threadBehindIcon.getComments().get(threadBehindIcon.getComments().size()-1).getContent();

        final int closestValidLine = getClosestValidLine(line, document);
        ApplicationManager.getApplication().invokeLater(() -> {
            RangeHighlighter highlighter = markupModel.addLineHighlighter(
                    closestValidLine, 0, null
            );
            highlighter.setGutterIconRenderer(new CommentGutterIconRenderer(commentIcon, toolTipp, threadBehindIcon, this) {
            });
        }, ModalityState.any());
    }

    private static int getClosestValidLine(int line, Document document) {
        if (line >= document.getLineCount()) {
            line = document.getLineCount() - 1;
        }
        if (line < 0) {
            line = 0;
        }
        return line;
    }

    private void createThreadOnServerAndModel(GitPullRequestCommentThread thread, Operation.Listener operationListener) {
        var createOperation = OperationFactory.creatPullRequestThreadCreateOperation(this.model.getRemoteUrl());
        var pullRequestId = this.model.getPullRequestId();

        createOperation.addListener(operationListener);
        var createOperationInput = new PullRequestThreadOperation.PullRequestThreadOperationInput(pullRequestId, thread);
        OperationExecutor.getInstance().executeAsync(createOperation, createOperationInput);
    }

    private void updateThreadOnServerAndModel(GitPullRequestCommentThread thread) {
        var updateOperation = OperationFactory.createPullRequestThreadUpdateOperation(this.model.getRemoteUrl());
        var pullRequestId = this.model.getPullRequestId();
        var threadId = thread.getId();

        updateOperation.addListener(new Operation.Listener() {
            @Override
            public void notifyLookupStarted() {

            }

            @Override
            public void notifyLookupCompleted() {

            }

            @Override
            public void notifyLookupResults(Operation.Results results) {
                var persistedThread = ((PullRequestThreadOperation.PullRequestThreadOperationResult) results).getGitPullRequestCommentThread();
                model.updateThreadsComments(persistedThread);
            }
        });

        var updateOperationInput = new PullRequestThreadOperation.PullRequestThreadOperationInput(pullRequestId, thread, threadId);
        OperationExecutor.getInstance().executeAsync(updateOperation, updateOperationInput);
    }

    @Override
    public void dispose() {
        view.dispose();
    }
}