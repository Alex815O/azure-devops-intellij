package com.microsoft.alm.plugin.idea.git.ui.pullrequest.codeReview.pullRequestComment;

import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import com.microsoft.alm.plugin.external.models.pullRequestThread.Comment;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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

        contentPane.setBorder(JBUI.Borders.empty(10));
        contentPane.setAutoscrolls(true);
        JBScrollPane scrollPane = new JBScrollPane(contentPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        setMinimumSize(new Dimension(550, 100));
        pack();
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
        JPanel commentPanel = new JPanel();
        commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.Y_AXIS));
        commentPanel.setBorder(BorderFactory.createEmptyBorder(5, 2, 2, 2));

        // Autor-Label (oben links)
        var authorLabel = new JLabel(comment.getAuthor().getDisplayName() + ":");
        authorLabel.setFont(UIManager.getFont("Label.font").deriveFont(Font.BOLD, 14));
        authorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        commentPanel.add(authorLabel);

        // Abstand zwischen Label und TextArea
        commentPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        // TextArea mit Scroll (nutzt verfügbaren Platz)
        var textArea = new JTextArea();
        textArea.setText(comment.getContent());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(UIManager.getFont("Label.font"));
        textArea.setOpaque(false);
        textArea.setBackground(new Color(0,0,0,0));

        JScrollPane scrollPane = new JBScrollPane(textArea);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setBorder(null);
        scrollPane.setViewportBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setBackground(new Color(0,0,0,0));

        commentPanel.add(scrollPane);

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
        commentPanel.setBorder(new MatteBorder(0, 0, 1, 0, JBColor.BLACK));

        int idx = 0;
        for (Comment comment : comments) {
            commentPanel.add(createCommentTextField(comment), idx++);
        }

        commentPanel.revalidate();
        commentPanel.repaint();
    }
}
