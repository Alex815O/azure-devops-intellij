package com.microsoft.alm.plugin.idea.git.ui.pullrequest.pullRequestComment;

import com.intellij.ui.components.JBScrollPane;
import com.microsoft.alm.plugin.external.models.pullRequestThread.Comment;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

        setTitle("Findings");
        contentPane.setPreferredSize(new Dimension(550, 400));
        JBScrollPane scrollPane = new JBScrollPane(contentPane);

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setContentPane(scrollPane);
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

        pack();
        setLocationRelativeTo(null);
    }

    private void onOK() {
        controller.addComment(this.commentField.getText());
        dispose();
    }

    private void onCancel() {
        controller.cancelComment();
        dispose();
    }

    private JPanel createCommentTextField(Comment comment) {
        JPanel commentPanel = new JPanel(new GridBagLayout());

        GridBagConstraints left = new GridBagConstraints();
        left.gridx = 0;
        left.gridy = 0;
        left.weightx = 0.1;
        left.weighty = 1.0;
        left.fill = GridBagConstraints.BOTH;

        GridBagConstraints right = new GridBagConstraints();
        right.gridx = 1;
        right.gridy = 0;
        right.weightx = 0.9;
        right.weighty = 1.0;
        right.fill = GridBagConstraints.BOTH;


        var textField = new JTextField();
        var authorLabel = new JLabel(comment.getAuthor().getDisplayName());

        textField.setText(comment.getContent());
        textField.setEditable(false);
        textField.setOpaque(false);
        textField.setFocusable(false);

        commentPanel.add(authorLabel, left);
        commentPanel.add(textField, right);

        return commentPanel;
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
