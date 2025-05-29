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
import com.microsoft.alm.plugin.idea.common.resources.Icons;
import com.microsoft.alm.plugin.idea.git.ui.pullrequest.pullRequestComment.marker.CommentGutterIconRenderer;
import com.microsoft.alm.plugin.operations.Operation;
import com.microsoft.alm.plugin.operations.OperationExecutor;
import com.microsoft.alm.plugin.operations.OperationFactory;
import com.microsoft.alm.plugin.operations.PullRequestThreadOperation;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

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

                threadsPerLine.keySet().forEach(line -> addGutterIcon(editor, line));
            }
        });
        OperationExecutor.getInstance().executeAsync(commentThreadListOperation, commentThreadListOperationInput);
    }

    public CommentingDiffViewer getView() {
        return view;
    }

    protected void createNewCommentDialog(EditorMouseEvent event, Editor editor) {
        int line = editor.xyToLogicalPosition(event.getMouseEvent().getPoint()).line;

        addGutterIcon(editor, line);
    }

    private void addGutterIcon(Editor editor, int line) {
        Document document = editor.getDocument();
        MarkupModel markupModel = editor.getMarkupModel();

        Icon commentIcon = Icons.PR_COMMENT;
        String toolTipp = "PullRequest comment";


        final int closestValidLine = getClosestValidLine(line, document);
        ApplicationManager.getApplication().invokeLater(() -> {
            RangeHighlighter highlighter = markupModel.addLineHighlighter(
                    closestValidLine, 0, null
            );
            highlighter.setGutterIconRenderer(new CommentGutterIconRenderer(commentIcon, toolTipp) {
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

    @Override
    public void dispose() {
        view.dispose();
    }
}