package com.microsoft.alm.plugin.idea.git.ui.pullrequest.pullRequestComment;

import com.microsoft.alm.plugin.external.models.pullRequestThread.Comment;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PullRequestCommentDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel commentPanel;
    private JTextField commentField;

    private final PullRequestCommentController controller;

    public PullRequestCommentDialog(PullRequestCommentController controller) {
        this.controller = controller;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        controller.addComment(this.commentField.getText());
        dispose();
    }

    private void onCancel() {
        controller.cancelComment();
        dispose();
    }

    private JTextField createCommentTextField(Comment comment) {
        var textPane = new JTextField();
        textPane.setText(comment.getContent());
        textPane.setEditable(false);
        textPane.setOpaque(false);
        textPane.setFocusable(false);
        return textPane;
    }

    public static void main(String[] args) {
        PullRequestCommentDialog dialog = new PullRequestCommentDialog(null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void createUIComponents() {
        var comments = this.controller.getComments();

        commentPanel = new JPanel();
        commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.Y_AXIS));

        int idx = 0;
        for (Comment comment : comments) {
            commentPanel.add(createCommentTextField(comment), idx++);
        }

        commentPanel.revalidate();
        commentPanel.repaint();
    }
}
