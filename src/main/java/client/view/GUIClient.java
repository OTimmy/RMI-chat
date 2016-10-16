package client.view;

import gcom.communicationmodule.NonReliableCommunication;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Hashtable;

/**
 * Causal ordering builds upon FIFO ordering.
 * If message B is broadcast from node P1 after message A has been delivered,
 * then message B must be delivered after message A is delivered, at all other nodes.
 */


/**
 * Created by c12ton on 9/29/16.
 */

public class GUIClient {

    private HashSet<String> leaderOf = new HashSet<>();
    private Hashtable<String, String> usernameInGroup = new Hashtable();

    private String host = null;

    private boolean ok = true;

    private ButtonGroup radioButtonsGroup = new ButtonGroup();

    private ButtonGroup debugButtonGroup = new ButtonGroup();
    private JRadioButton nonDebugRB = new JRadioButton("Non-debug mode");
    private JRadioButton debugRB = new JRadioButton("Debug mode");

    private ButtonGroup orderButtonGroup = new ButtonGroup();
    private JRadioButton noOrderRB = new JRadioButton("No order");
    private JRadioButton casualOrderRB = new JRadioButton("Casual order");

    private JFrame frame = new JFrame("GUIClient");
    private JPanel groupInfoPane = new JPanel();
    private JPanel inputButtonsPane = new JPanel();

    private JPanel groupTab = new JPanel();

    private JTabbedPane tabbedPane = new JTabbedPane();
    private JButton joinGroupButton = new JButton("Join");
    private JTextField groupNameInput = new JTextField();

    private JTextField hostTextField = new JTextField(10);

    private JButton refreshGroupButton = new JButton("Refresh");

    private JRadioButton basicNonReliableRadioButton = new JRadioButton("Basic non-reliable");
    private JRadioButton basicReliableRadioButton = new JRadioButton("Basic reliable");
    private JRadioButton treeBasedReliableRadioButton = new JRadioButton("tree-based reliable");
    private JButton createGroupButton = new JButton("Create Group");

    private JTextField usernameTextField = new JTextField(20);
    private JButton deleteGroupButton = new JButton("Delete");
    private JTable groupTable;
    private int MAXIMUM_TABS = 10;
    private DefaultTableModel tableModel = new DefaultTableModel();


    public GUIClient() {

        debugButtonGroup.add(nonDebugRB);
        debugButtonGroup.add(debugRB);

        nonDebugRB.setSelected(true);

        orderButtonGroup.add(noOrderRB);
        orderButtonGroup.add(casualOrderRB);

        noOrderRB.setSelected(true);

        hostTextField.setText("localhost");
        setHost();

        if (host == (null)) {
            System.exit(0);
        }

        //check if connection is available
        //if not then ask again


        groupTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        groupTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = groupTable.getSelectedRow();
                if (index != -1) {

                    if (leaderOf.contains(groupTable.getValueAt(index, 0))) {
                        joinGroupButton.setEnabled(false);
                        deleteGroupButton.setEnabled(true);
                    } else {
                        joinGroupButton.setEnabled(true);
                        deleteGroupButton.setEnabled(false);
                    }
                }

            }
        });

        groupTable.setBorder(BorderFactory.createLineBorder(Color.black));
        groupTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        JScrollPane gsp = new JScrollPane(groupTable);
        gsp.setAutoscrolls(true);
        tableModel.addColumn("Groups");

        joinGroupButton.setEnabled(false);
        deleteGroupButton.setEnabled(false);
        createGroupButton.setEnabled(false);

        usernameTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if(usernameTextField.getText() == ""){
                    createGroupButton.setEnabled(false);
                    return;
                }
                createGroupButton.setEnabled(true);

            }

            @Override
            public void removeUpdate(DocumentEvent e) {

                if(usernameTextField.getText().equals("")){
                    createGroupButton.setEnabled(false);
                    return;
                }
                createGroupButton.setEnabled(true);

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });


        inputButtonsPane.add(new JLabel("Username *"));
        inputButtonsPane.add(usernameTextField);
        inputButtonsPane.add(joinGroupButton);
        inputButtonsPane.add(deleteGroupButton);
        inputButtonsPane.add(createGroupButton);

        groupInfoPane.setLayout(new BorderLayout());
        groupInfoPane.add(gsp, BorderLayout.CENTER);
        groupInfoPane.add(inputButtonsPane, BorderLayout.NORTH);

        groupNameInput.setMaximumSize(new Dimension(350, 20));

        radioButtonsGroup.add(basicNonReliableRadioButton);
        radioButtonsGroup.add(basicReliableRadioButton);
        radioButtonsGroup.add(treeBasedReliableRadioButton);

        basicReliableRadioButton.setEnabled(false);
        treeBasedReliableRadioButton.setEnabled(false);
        refreshGroupButton.setPreferredSize(new Dimension(90, 30));

        JPanel refresh = new JPanel();
        refresh.add(refreshGroupButton);

        groupTab.setLayout(new BorderLayout(2, 2));
        groupTab.add(groupInfoPane, BorderLayout.CENTER);
        groupTab.add(refresh, BorderLayout.SOUTH);

        tabbedPane.addTab("Groups", groupTab);

        frame.add(tabbedPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(600, 400));
        //frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);

    }

    public boolean getIfNoOrder(){
        return noOrderRB.isSelected();
    }


    public boolean getIfDebug(){
        return debugRB.isSelected();
    }

    public void showErrorMess(String s) {
        JOptionPane.showMessageDialog(tabbedPane, s);
    }

    public void setHost() {
        new HostDialog();
        host = hostTextField.getText();
    }

    public String inputHostMessage() {
        return JOptionPane.showInputDialog(tabbedPane, "Enter Host:", "localhost");
    }

    public String getHost() {
        return host;
    }

    public Boolean myNameInGroup(String group, String userName) {
        if (usernameInGroup.get(group).equals(userName)) {
            return true;
        }
        return false;
    }

    private JPanel chattTab(String group) {

        usernameInGroup.put(group, usernameTextField.getText());

        JTextArea membersList = new JTextArea(3, 16);
        membersList.setEditable(false);
        JScrollPane spm = new JScrollPane(membersList);


        JTextArea chattArea = new JTextArea(3, 16);
        chattArea.setEditable(false);

        DefaultCaret caret = (DefaultCaret) chattArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane spc = new JScrollPane(chattArea);

        JTextArea messageArea = new JTextArea(4, 45);

        JButton sendButton = new JButton("Send");
        sendButton.setName(group + "/" + getName());
        sendButton.setPreferredSize(new Dimension(80, 62));

        messageArea.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    sendButton.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        JScrollPane textPane = new JScrollPane(messageArea);
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


    public String addGroupTab() {

        String s = groupNameInput.getText();

        if (!s.equals("") && (tabbedPane.getTabCount() < MAXIMUM_TABS)) {

            ButtonModel comunication = radioButtonsGroup.getSelection();

            if (comunication == null) {
                JOptionPane.showMessageDialog(tabbedPane, "Choose one of the communication types");
                return null;
            } else if (usernameTextField.getText().equals("")) {
                JOptionPane.showMessageDialog(tabbedPane, "Enter username");
                return null;
            }

            tabbedPane.addTab(s, chattTab(s));
            tableModel.addRow(new Object[]{s});

            leaderOf.add(s);

            shiftFocusToTab();

            return s;
        } else if (tabbedPane.getTabCount() >= MAXIMUM_TABS) {
            JOptionPane.showMessageDialog(tabbedPane, "Too many tabs open");
        } else {
            JOptionPane.showMessageDialog(tabbedPane, "Fill in the groupname!");
        }
        return null;
    }

    public void addJoinTab(String group) {
        if (!group.equals("") && (tabbedPane.getTabCount() < MAXIMUM_TABS)) {

            tabbedPane.addTab(group, chattTab(group));
        }
    }

    public String joinGroup() {

        String username = usernameTextField.getText();
        if (username.equals("")) {
            JOptionPane.showMessageDialog(tabbedPane, "Enter Username");
            return null;
        }


        int row = groupTable.getSelectedRow();
        if (row > -1) {
            String group = (String) groupTable.getValueAt(groupTable.getSelectedRow(), 0);

            return username + "/" + group;
        }
        return null;
    }


    public void appendMessage(String group, String message) {

                JTextArea ta = getTextArea(group, 1);

                ta.append(message);
    }

    public String getMessage(String group) {
        for (int i = tabbedPane.getTabCount() - 1; i >= 1; i--) {

            JComponent tp = (JComponent) tabbedPane.getComponentAt(i);

            if (tabbedPane.getTitleAt(i).equals(group)) {
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

        if (groupTable.getRowCount() > 0) {
            int row = groupTable.getSelectedRow();
            if (row != -1) {
                String groupName = (String) tableModel.getValueAt(row, 0);
                tableModel.removeRow(row);

                for (int i = tabbedPane.getTabCount() - 1; i >= 1; i--) {
                    if (tabbedPane.getTitleAt(i).equals(groupName)) {
                        tabbedPane.removeTabAt(i);
                    }
                }
            }
        }
    }

    public void updateGroups(String[] groups) {
        groupTable.clearSelection();

        for (int i = groupTable.getRowCount() - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }

        for (String group : groups) {
            tableModel.addRow(new Object[]{group});
        }
    }

    public void addActionListenerJoin(ActionListener a) {
        joinGroupButton.addActionListener(a);
    }

    public void addActionListenerDelete(ActionListener a) {
        deleteGroupButton.addActionListener(a);
    }

    public void addActionListenerCreate(ActionListener a) {
        createGroupButton.addActionListener(a);
    }

    public void addActionListenerSend(String group, ActionListener a) {

        for (int i = tabbedPane.getTabCount() - 1; i >= 1; i--) {

            JComponent tp = (JComponent) tabbedPane.getComponentAt(i);

            if (tabbedPane.getTitleAt(i).equals(group)) {
                JPanel sp = (JPanel) tp.getComponent(2);

                JButton send = (JButton) sp.getComponent(1);
                send.addActionListener(a);
            }
        }
    }

    public void addActionListererRefresh(ActionListener a) {
        refreshGroupButton.addActionListener(a);
    }

    public Class getCom() {

        if (basicNonReliableRadioButton.isSelected()) {
            return NonReliableCommunication.class.getClass();
        }
        return null;
    }

    public void setLeaderOf(String group) {
        leaderOf.add(group);
    }

    private JTextArea getTextArea(String group, int index) {

        JTextArea ta = null;
        for (int i = tabbedPane.getTabCount() - 1; i >= 1; i--) {

            JComponent tp = (JComponent) tabbedPane.getComponentAt(i);

            if (tabbedPane.getTitleAt(i).equals(group)) {
                JScrollPane sp = (JScrollPane) tp.getComponent(index);

                JViewport viewport = sp.getViewport();
                ta = (JTextArea) viewport.getView();
            }
        }

        return ta;

    }


    public void setMembers(String group, String[] members) {

        JTextArea ta = getTextArea(group, 0);
        for (String m : members) {

            if(myNameInGroup(group, m)){
                m = "--> " + m;
            }

            ta.append(m + "\n");
        }

    }

    public void removeMember(String group, String name) {

        JTextArea ta = getTextArea(group, 0);
        String[] data = ta.getText().split("\n");

        ta.setText("");
        for (int j = 0; j < data.length; j++) {
            if (!data[j].equals(name)) {
                ta.append(data[j]);
            }
        }
    }


    public void addMember(String group, String name) {
                JTextArea ta = getTextArea(group, 0);

                if(!myNameInGroup(group, name)) {
                    ta.append(name + "\n");
                }
    }

    public String showGroupCreation() {
        new JGroupCreation();
        if (ok) {
            return groupNameInput.getText();
        }
        return null;
    }

    public String getName() {
        return usernameTextField.getText();
    }

    public void shiftFocusToTab() {
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
    }


    public class JGroupCreation extends JDialog {

        public JGroupCreation() {

            JPanel panelg = new JPanel(new GridLayout(0, 1));

            panelg.add(new JLabel("Select commuication"));

            basicNonReliableRadioButton.setSelected(true);

            panelg.add(basicNonReliableRadioButton);
            panelg.add(basicReliableRadioButton);
            panelg.add(treeBasedReliableRadioButton);

            panelg.add(Box.createVerticalStrut(10));
            panelg.add(new JLabel("Select ordering"));

            panelg.add(noOrderRB);
            panelg.add(casualOrderRB);

            panelg.add(Box.createVerticalStrut(10));

            JLabel groupLable = new JLabel("Groupname:");
            panelg.add(groupLable);
            panelg.add(groupNameInput);

            Boolean occupied = true;

            while (occupied) {
                int result = JOptionPane.showConfirmDialog(frame, panelg, "Create Group",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (groupTable.getRowCount() == 0) {
                    occupied = false;
                }

                for (int i = groupTable.getRowCount() - 1; i >= 0; i--) {
                    if (groupTable.getValueAt(i, 0).equals(groupNameInput.getText())) {
                        groupLable.setText("Groupname: " +
                                "Groupname was in use!");
                        groupLable.setForeground(Color.RED);
                        occupied = true;
                        break;
                    }
                    occupied = false;
                }


                if (result == JOptionPane.OK_OPTION && usernameTextField.getText() != null) {
                    ok = true;
                } else {
                    ok = false;
                    return;
                }
            }

        }
    }

    public class HostDialog {

        public HostDialog() {

            JPanel panel = new JPanel(new GridLayout(0, 1));
            JPanel panel2 = new JPanel();
            panel2.add(new JLabel("Enter host:"));
            panel2.add(hostTextField);

            JPanel panel3 = new JPanel();
            panel3.add(nonDebugRB);
            panel3.add(debugRB);

            panel.add(panel2);
            panel.add(panel3);
            int result = JOptionPane.showOptionDialog(tabbedPane, panel, "Enter Host",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, null, null);
            if (result == JOptionPane.CANCEL_OPTION) {
                System.exit(0);
            }
        }
    }
}
