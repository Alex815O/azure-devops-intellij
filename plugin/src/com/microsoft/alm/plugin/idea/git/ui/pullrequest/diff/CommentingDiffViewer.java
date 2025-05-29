package com.microsoft.alm.plugin.idea.git.ui.pullrequest.diff;

import com.intellij.diff.DiffContext;
import com.intellij.diff.requests.DiffRequest;
import com.intellij.diff.tools.simple.SimpleDiffViewer;
import com.intellij.diff.util.Side;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorMouseAdapter;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseEventArea;
import org.jetbrains.annotations.NotNull;

public class CommentingDiffViewer extends SimpleDiffViewer {

    private final CommentingDiffController controller;

    public CommentingDiffViewer(@NotNull DiffContext context, @NotNull DiffRequest request, CommentingDiffController controller) {
        super(context, request);

        this.controller = controller;

        var editor = this.getEditor();
        editor.addEditorMouseListener(new EditorMouseAdapter() {
            @Override
            public void mouseClicked(EditorMouseEvent event) {
                EditorMouseEventArea area = event.getArea();
                if (isInGatter(area)) {

                    controller.createNewCommentDialog(event, editor);
                }
            }

            private boolean isInGatter(EditorMouseEventArea area) {
                return area == EditorMouseEventArea.LINE_NUMBERS_AREA
                        || area == EditorMouseEventArea.LINE_MARKERS_AREA
                        || area == EditorMouseEventArea.ANNOTATIONS_AREA
                        || area == EditorMouseEventArea.FOLDING_OUTLINE_AREA;
            }
        });
    }

    public Editor getEditor() {
        return super.getEditor(Side.RIGHT);
    }

}
