package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by c12ton on 9/29/16.
 */

public class GUIClient {

    private String host;

    private JFrame frame = new JFrame("GUIClient");
    private JPanel tablePane = new JPanel();
    private JPanel userJoinDeletePane = new JPanel();

    private JPanel groupsPanel = new JPanel();

    private JTabbedPane tabbedPane1 = new JTabbedPane();
    private JButton joinGroupButton = new JButton("Join");
    private JTextField groupNameTextField = new JTextField();

    private JLabel label = new JLabel("Create group");
    private JRadioButton basicNonReliableRadioButton = new JRadioButton("Basic non-reliable");
    private JRadioButton basicReliableRadioButton = new JRadioButton("Basic reliable");
    private JRadioButton treeBasedReliableRadioButton = new JRadioButton("tree-based reliable");
    private JButton createGroupButton = new JButton("Create Group");

    private JTextField usernameTextField = new JTextField(20);
    private JButton deleteGroupButton = new JButton("Delete");
    private JTable table1;
    private int MAXIMUM_TABS = 9;
    private DefaultTableModel model = new DefaultTableModel();


    public GUIClient() {

        String s = JOptionPane.showInputDialog(tabbedPane1, "Enter Host:");
        //check if connection is available
        //if not then ask again
        host = s;


        table1 = new JTable(model);
        table1.setBorder(BorderFactory.createLineBorder(Color.black));
        model.addColumn("Groups");

        userJoinDeletePane.add(usernameTextField);
        userJoinDeletePane.add(joinGroupButton);
        userJoinDeletePane.add(deleteGroupButton);

        tablePane.setLayout(new BorderLayout());
        tablePane.add(table1, BorderLayout.CENTER);
        tablePane.add(userJoinDeletePane, BorderLayout.SOUTH);

        groupNameTextField.setMaximumSize(new Dimension(350, 20));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(label);
        panel.add(basicNonReliableRadioButton);
        panel.add(basicReliableRadioButton);
        panel.add(treeBasedReliableRadioButton);
        panel.add(groupNameTextField);
        panel.add(createGroupButton);


        groupsPanel.setLayout(new BorderLayout(2,2));
        groupsPanel.add(tablePane, BorderLayout.CENTER);
        groupsPanel.add(panel, BorderLayout.EAST);


        tabbedPane1.addTab("Groups", groupsPanel);

        frame.add(tabbedPane1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(600,400));
        //frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);

    }

    private JPanel chattTab(String group){

        JTextArea membersList = new JTextArea(3, 16);
        membersList.setEditable(false);
        JScrollPane spm = new JScrollPane(membersList);


        JTextArea chattArea = new JTextArea(3, 16);
        chattArea.setEditable(false);
        JScrollPane spc = new JScrollPane(chattArea);

        JButton sendButton = new JButton("Send");
        sendButton.setName(group);
        JTextArea messageArea = new JTextArea( 4, 45);
        JScrollPane textPane= new JScrollPane(messageArea);
        JPanel messagePane = new JPanel();
        messagePane.add(textPane);
        messagePane.add(sendButton);


        JPanel chattPanel = new JPanel();
        chattPanel.setLayout(new BorderLayout());
        chattPanel.add(spm, BorderLayout.EAST);
        chattPanel.add(spc, BorderLayout.CENTER);
        chattPanel.add(messagePane, BorderLayout.SOUTH);


        return chattPanel;
    }


    public String addGroupTab(){

        String s = groupNameTextField.getText();

        if(!s.equals("") && (tabbedPane1.getTabCount() < MAXIMUM_TABS)){
            tabbedPane1.addTab(groupNameTextField.getText(), chattTab(s));
            model.addRow(new Object[] {s});

            getMessage(s);

            return s;
        }
        else if(tabbedPane1.getTabCount() >= MAXIMUM_TABS){
            JOptionPane.showMessageDialog(tabbedPane1, "Too many tabs open");
        }
        else{
            JOptionPane.showMessageDialog(tabbedPane1, "Fill in the groupname!");
        }
        return null;
    }


    public void appendMessage(String group, String message){

        for(int i = tabbedPane1.getTabCount()-1; i >= 1; i--){

            JComponent tp = (JComponent) tabbedPane1.getComponentAt(i);

            if(tabbedPane1.getTitleAt(i).equals(group)){
                JScrollPane sp = (JScrollPane) tp.getComponent(1);

                JViewport viewport = sp.getViewport();
                JTextArea ta = (JTextArea) viewport.getView();

                ta.append(message);
            }
        }

    }

    public String getMessage(String group) {
        for(int i = tabbedPane1.getTabCount()-1; i >= 1; i--){

            JComponent tp = (JComponent) tabbedPane1.getComponentAt(i);

            if(tabbedPane1.getTitleAt(i).equals(group)){
                JPanel sp = (JPanel) tp.getComponent(2);

                JScrollPane spTextFiled = (JScrollPane) sp.getComponent(0);

                JViewport viewport = spTextFiled.getViewport();
                JTextArea ta = (JTextArea) viewport.getView();

                String msg = ta.getText();

                ta.setText("");
                return msg;
            }
        }
        return null;
    }

    public void addActionListenerJoin(ActionListener a){
        joinGroupButton.addActionListener(a);
    }

    public void addActionListenerDelete(ActionListener a){
        deleteGroupButton.addActionListener(a);
    }

    public void addActionListenerCreate(ActionListener a){
        createGroupButton.addActionListener(a);
    }

    public void addActionListenerSend(String group, ActionListener a){

        for(int i = tabbedPane1.getTabCount()-1; i >= 1; i--){

            JComponent tp = (JComponent) tabbedPane1.getComponentAt(i);

            if(tabbedPane1.getTitleAt(i).equals(group)){
                JPanel sp = (JPanel) tp.getComponent(2);

                JButton send = (JButton) sp.getComponent(1);
                send.addActionListener(a);
            }
        }
    }



}
