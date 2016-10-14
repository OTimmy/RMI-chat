package client.view;

import com.sun.org.apache.xpath.internal.operations.Bool;
import gcom.communicationmodule.NonReliableCommunication;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Hashtable;

import static javax.swing.BoxLayout.Y_AXIS;

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
    private Hashtable<String, String> usernameInGroup= new Hashtable();

    private String host = null;

    private boolean debug = false;
    private boolean ok = true;

    public boolean isOk() {
        return ok;
    }

    private ButtonGroup radioButtonsGroup = new ButtonGroup();

    private JFrame frame = new JFrame("GUIClient");
    private JFrame debugFrame = new JFrame("Debug");
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

        if(debug) {
            debugClient();
        }

    }

    public void debugClient(){

        DefaultTableModel groupsModel = new DefaultTableModel();
        DefaultTableModel messModel   = new DefaultTableModel();
        DefaultTableModel vectorModel = new DefaultTableModel();

        JPanel debugPane = new JPanel(new BorderLayout());

        JTable debugGroups = new JTable(groupsModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane groupsSp = new JScrollPane(debugGroups);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel g = new JLabel("Groups");
        g.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
        groupPanel.add(g);
        groupPanel.add(groupsSp);
        groupPanel.add(refreshButton);


        JButton hold = new JButton("Hold messages");
        JPanel button = new JPanel();
        button.add(hold);

        JTable messTable = new JTable(messModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        messModel.addColumn("Member");
        messModel.addColumn("Message");
        JScrollPane scrollMessPane = new JScrollPane(messTable);

        JTable vectorTable = new JTable(vectorModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vectorModel.addColumn("Member");
        vectorModel.addColumn("Vector");
        JScrollPane scrollvectorPane = new JScrollPane(vectorTable);

        JPanel tableBPane = new JPanel();
        tableBPane.setLayout(new BoxLayout(tableBPane, BoxLayout.Y_AXIS));
        tableBPane.add(button);
        tableBPane.add(scrollMessPane);
        tableBPane.add(scrollvectorPane);

        debugPane.add(tableBPane, BorderLayout.WEST);
        debugPane.add(groupPanel, BorderLayout.EAST);

        debugFrame.add(debugPane);
        debugFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        debugFrame.setMinimumSize(new Dimension(600, 400));
        debugFrame.setResizable(false);
        debugFrame.pack();
        debugFrame.setVisible(true);
    }

    public void showErrorMess(String s) {
        JOptionPane.showMessageDialog(tabbedPane, s);
    }

    public void setHost() {
        new TestDialog();
        host = hostTextField.getText();
    }

    public String inputHostMessage() {
        return JOptionPane.showInputDialog(tabbedPane, "Enter Host:", "localhost");
    }

    public String getHost() {
        return host;
    }

    public Boolean myNameInGroup(String group, String userName){
        if(usernameInGroup.get(group).equals(userName)){
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
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    sendButton.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
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

        for (int i = tabbedPane.getTabCount() - 1; i >= 1; i--) {

            JComponent tp = (JComponent) tabbedPane.getComponentAt(i);

            if (tabbedPane.getTitleAt(i).equals(group)) {
                JScrollPane sp = (JScrollPane) tp.getComponent(1);

                JViewport viewport = sp.getViewport();
                JTextArea ta = (JTextArea) viewport.getView();

                ta.append(message);
            }
        }

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

                System.out.println("IM HERE");

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

        System.out.println(groupNameInput.getText());

        if (basicNonReliableRadioButton.isSelected()) {
            return NonReliableCommunication.class.getClass();
        }
        return null;
    }

    public void setLeaderOf(String group) {
        leaderOf.add(group);
    }

    public void removeLeaderOf(String group) {
        leaderOf.remove(group);
    }


    public void setMembers(String group, String[] members) {

        for (int i = tabbedPane.getTabCount() - 1; i >= 1; i--) {

            JComponent tp = (JComponent) tabbedPane.getComponentAt(i);

            if (tabbedPane.getTitleAt(i).equals(group)) {
                JScrollPane sp = (JScrollPane) tp.getComponent(0);

                JViewport viewport = sp.getViewport();
                JTextArea ta = (JTextArea) viewport.getView();
                for (String m : members) {
                    ta.append(m + "\n");
                }
            }
        }
    }

    public void removeMember(String group, String name) {
        for (int i = tabbedPane.getTabCount() - 1; i >= 1; i--) {

            JComponent tp = (JComponent) tabbedPane.getComponentAt(i);

            if (tabbedPane.getTitleAt(i).equals(group)) {
                JScrollPane sp = (JScrollPane) tp.getComponent(0);

                JViewport viewport = sp.getViewport();
                JTextArea ta = (JTextArea) viewport.getView();
                String[] data = ta.getText().split("\n");

                ta.setText("");
                for(int j = 0; j < data.length; j--){
                    if(!data[i].equals(name)){
                        ta.append(data[i]);
                    }
                }
            }
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


    public class JGroupCreation extends JDialog {

        public JGroupCreation() {

            JPanel panelg = new JPanel(new GridLayout(0, 1));

            panelg.add(new JLabel("Select commuication"));

            /*temp*/
            basicNonReliableRadioButton.setSelected(true);

            panelg.add(basicNonReliableRadioButton);
            panelg.add(basicReliableRadioButton);
            panelg.add(treeBasedReliableRadioButton);

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


                if (result == JOptionPane.OK_OPTION) {
                    System.out.println("info");
                    ok = true;
                } else {
                    System.out.println("Cancelled");
                    ok = false;
                    return;
                }
            }
        }
    }

    public class TestDialog {

        public TestDialog() {
            Object[] options1 = { "Client", "Debug",
                    "Cancel" };

            JPanel panel = new JPanel();
            panel.add(new JLabel("Enter host:"));


            panel.add(hostTextField);

            int result = JOptionPane.showOptionDialog(tabbedPane, panel, "Enter Host",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options1, null);
            if (result == JOptionPane.YES_OPTION){
                debug = false;
                //Client
            }else if (result == JOptionPane.NO_OPTION){
                debug = true;
                //Debug
            }else if (result == JOptionPane.CANCEL_OPTION){
                System.exit(0);
            }
        }
    }
}
