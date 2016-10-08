package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by c12ton on 9/29/16.
 */

public class GUIClient {

    private String host = null;

    private ButtonGroup radioButtonsGroup = new ButtonGroup();

    private JFrame frame = new JFrame("GUIClient");
    private JPanel groupInfoPane = new JPanel();
    private JPanel inputButtonsPane = new JPanel();

    private JPanel groupTab = new JPanel();

    private JTabbedPane tabbedPane = new JTabbedPane();
    private JButton joinGroupButton = new JButton("Join");
    private JTextField groupNameInput = new JTextField();

    private JLabel createLabel = new JLabel("Create group");
    private JRadioButton basicNonReliableRadioButton = new JRadioButton("Basic non-reliable");
    private JRadioButton basicReliableRadioButton = new JRadioButton("Basic reliable");
    private JRadioButton treeBasedReliableRadioButton = new JRadioButton("tree-based reliable");
    private JButton createGroupButton = new JButton("Create Group");

    private JTextField usernameTextField = new JTextField(20);
    private JButton deleteGroupButton = new JButton("Delete");
    private JTable groupTable;
    private int MAXIMUM_TABS = 9;
    private DefaultTableModel tableModel = new DefaultTableModel();


    public GUIClient() {

        host = JOptionPane.showInputDialog(tabbedPane, "Enter Host:", "localhost");

        if(host == (null)){
            return;
        }

        //check if connection is available
        //if not then ask again


        groupTable = new JTable(tableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        groupTable.setBorder(BorderFactory.createLineBorder(Color.black));
        tableModel.addColumn("Groups");

        inputButtonsPane.add(usernameTextField);
        inputButtonsPane.add(joinGroupButton);
        inputButtonsPane.add(deleteGroupButton);

        groupInfoPane.setLayout(new BorderLayout());
        groupInfoPane.add(groupTable, BorderLayout.CENTER);
        groupInfoPane.add(inputButtonsPane, BorderLayout.SOUTH);

        groupNameInput.setMaximumSize(new Dimension(350, 20));

        radioButtonsGroup.add(basicNonReliableRadioButton);
        radioButtonsGroup.add(basicReliableRadioButton);
        radioButtonsGroup.add(treeBasedReliableRadioButton);

        basicReliableRadioButton.setEnabled(false);
        treeBasedReliableRadioButton.setEnabled(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createLabel);
        panel.add(basicNonReliableRadioButton);
        panel.add(basicReliableRadioButton);
        panel.add(treeBasedReliableRadioButton);
        panel.add(groupNameInput);
        panel.add(createGroupButton);


        groupTab.setLayout(new BorderLayout(2,2));
        groupTab.add(groupInfoPane, BorderLayout.CENTER);
        groupTab.add(panel, BorderLayout.EAST);


        tabbedPane.addTab("Groups", groupTab);

        frame.add(tabbedPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(600,400));
        //frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);

    }

    public String getHost() {
        return host;
    }

    private JPanel chattTab(String group){

        JTextArea membersList = new JTextArea(3, 16);
        membersList.setEditable(false);
        JScrollPane spm = new JScrollPane(membersList);


        JTextArea chattArea = new JTextArea(3, 16);
        chattArea.setEditable(false);
        JScrollPane spc = new JScrollPane(chattArea);

        JTextArea messageArea = new JTextArea( 4, 45);
        JButton sendButton = new JButton("Send");
        sendButton.setName(group);
        sendButton.setPreferredSize(new Dimension(80,62));
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

        String s = groupNameInput.getText();

        if(!s.equals("") && (tabbedPane.getTabCount() < MAXIMUM_TABS)){

            ButtonModel comunication = radioButtonsGroup.getSelection();

            if(comunication == null){
                JOptionPane.showMessageDialog(tabbedPane, "you must choose one of the communication types");
                return null;
            }

            tabbedPane.addTab(s, chattTab(s));
            tableModel.addRow(new Object[] {s});


            //getMessage(s);

            return s;
        }
        else if(tabbedPane.getTabCount() >= MAXIMUM_TABS){
            JOptionPane.showMessageDialog(tabbedPane, "Too many tabs open");
        }
        else{
            JOptionPane.showMessageDialog(tabbedPane, "Fill in the groupname!");
        }
        return null;
    }

    public void addJoinTab(String group){
        if(!group.equals("") && (tabbedPane.getTabCount() < MAXIMUM_TABS)) {

            ButtonModel comunication = radioButtonsGroup.getSelection();

            if (comunication == null) {
                JOptionPane.showMessageDialog(tabbedPane, "you must choose one of the communication types");
                return;
            }

            tabbedPane.addTab(group, chattTab(group));
        }
    }

    public String joinGroup(){

        String username = usernameTextField.getText();
        if(username.equals("")){
            JOptionPane.showMessageDialog(tabbedPane, "Enter Username");
        }


        int row = groupTable.getSelectedRow();
        if(row > -1){
            String group = (String) groupTable.getValueAt(groupTable.getSelectedRow(), 0);

            return username + "/" + group;
        }
        return null;
    }


    public void appendMessage(String group, String message){

        for(int i = tabbedPane.getTabCount()-1; i >= 1; i--){

            JComponent tp = (JComponent) tabbedPane.getComponentAt(i);

            if(tabbedPane.getTitleAt(i).equals(group)){
                JScrollPane sp = (JScrollPane) tp.getComponent(1);

                JViewport viewport = sp.getViewport();
                JTextArea ta = (JTextArea) viewport.getView();

                ta.append(message);
            }
        }

    }

    public String getMessage(String group) {
        for(int i = tabbedPane.getTabCount()-1; i >= 1; i--){

            JComponent tp = (JComponent) tabbedPane.getComponentAt(i);

            if(tabbedPane.getTitleAt(i).equals(group)){
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

    public void deleteGroup() {

        if (groupTable.getRowCount() > 0){
            int row = groupTable.getSelectedRow();
            if(row != -1){
                String group = (String) groupTable.getValueAt(row,0);
                //System.out.println(group);

                tableModel.removeRow(row);
                tabbedPane.removeTabAt(row+1);
            }
        }
    }

    public void updateGroups(String[] groups){

        for(int i = groupTable.getRowCount(); i > 0; i--){
            tableModel.removeRow(i);
        }

        for (String group:groups) {
            tableModel.addRow(new Object[]{group});
        }
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

        for(int i = tabbedPane.getTabCount()-1; i >= 1; i--){

            JComponent tp = (JComponent) tabbedPane.getComponentAt(i);

            if(tabbedPane.getTitleAt(i).equals(group)){
                JPanel sp = (JPanel) tp.getComponent(2);

                JButton send = (JButton) sp.getComponent(1);
                send.addActionListener(a);
            }
        }
    }
}
