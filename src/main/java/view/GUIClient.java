package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by c13slk on 2016-09-29.
 */
public class GUIClient {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JTextArea textArea1;
    private JTextField textField1;
    private JTextArea textArea2;
    private JButton sendButton;
    private JButton selectGroupButton;
    private JTextField groupNameTextField;
    private JRadioButton basicNonReliableRadioButton;
    private JRadioButton basicReliableRadioButton;
    private JRadioButton treeBasedReliableRadioButton;
    private JButton addGroupButton;
    private JTextField usernameTextField;
    private JButton deleteGroupButton;
    private JTextArea textArea3;

    private JPanel chatt = new JPanel();



    public GUIClient() {
        addGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane1.addTab("hej", panel1);
            }
        });
    }

    public static void main(String[] args) {



    }
}
