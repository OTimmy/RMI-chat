package gcomdebug.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by c13slk on 2016-10-14.
 */
public class DebugClient {

    private String host;

    private JFrame debugFrame = new JFrame("Debug");
    private DefaultTableModel groupsModel = new DefaultTableModel();
    private DefaultTableModel incommingModel = new DefaultTableModel();
    private DefaultTableModel outgoingModel = new DefaultTableModel();
    private DefaultTableModel vectorModel = new DefaultTableModel();

    private JButton ds = new JButton("Drop selected");
    private JButton ra = new JButton("Release all");
    private JButton refreshButton;

    private JTable debugGroups;
    private JTable incommingTable;
    private JTable outgoingTable;

    public DebugClient() {

        setHost();

        JPanel debugPane = new JPanel(new BorderLayout());

        debugGroups = new JTable(groupsModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane groupsSp = new JScrollPane(debugGroups);

        refreshButton = new JButton("Refresh");
        refreshButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel g = new JLabel("Groups");
        g.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
        groupPanel.add(g);
        groupPanel.add(groupsSp);
        groupPanel.add(refreshButton);

        //--------------------------------------------------------------------->

        JPanel buttons = new JPanel();
        buttons.add(ds);
        buttons.add(ra);

        incommingTable = new JTable(incommingModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        incommingModel.addColumn("Member");
        incommingModel.addColumn("Message");
        JScrollPane scrollIncommingPane = new JScrollPane(incommingTable);

        JPanel incWL = new JPanel();
        incWL.setLayout(new BoxLayout(incWL, BoxLayout.Y_AXIS));
        JLabel incLabel = new JLabel("Incomming");
        incLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        incWL.add(incLabel);
        incWL.add(scrollIncommingPane);

        outgoingTable = new JTable(outgoingModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        outgoingTable.setEnabled(false);//------------------------------------------------------------->
        outgoingModel.addColumn("Member");
        outgoingModel.addColumn("Message");
        JScrollPane scrollOutgoingPane = new JScrollPane(outgoingTable);

        JPanel outWL = new JPanel();
        outWL.setLayout(new BoxLayout(outWL, BoxLayout.Y_AXIS));
        JLabel outLabel = new JLabel("Outgoing");
        outLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        outWL.add(outLabel);
        outWL.add(scrollOutgoingPane);

        JPanel incOutPane = new JPanel();
        incOutPane.setLayout(new BoxLayout(incOutPane, BoxLayout.X_AXIS));
        incOutPane.add(incWL);
        incOutPane.add(outWL);


        JTable vectorTable = new JTable(vectorModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vectorTable.setEnabled(false);//------------------------------------------------------------->
        vectorModel.addColumn("Member");
        vectorModel.addColumn("Vector");
        JScrollPane scrollvectorPane = new JScrollPane(vectorTable);

        JPanel tableBPane = new JPanel();
        tableBPane.setLayout(new BoxLayout(tableBPane, BoxLayout.Y_AXIS));
        tableBPane.add(incOutPane);
        tableBPane.add(buttons);
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

    public void addIncomming(String member, String message){
        incommingModel.addRow(new Object[]{member, message});

    }

    public void addOutgoing(String member, String message){
        outgoingModel.addRow(new Object[]{member, message});
    }

    public void removeFromOutgoing(int row){
        outgoingModel.removeRow(row);
    }

    public void addVector(String mem, String vector){
        vectorModel.addRow(new Object[]{mem, vector});
    }

    public void removeVector(String mem){
        for(int i = 0; i<vectorModel.getRowCount(); i++){
            if(vectorModel.getValueAt(i, 0).equals(mem)){
                vectorModel.removeRow(i);
            }
        }
    }

    public void updateVector(String mem, String vector){

        for(int i = 0; i<vectorModel.getRowCount(); i++){
            if(vectorModel.getValueAt(i, 0).equals(mem)){
                vectorModel.setValueAt(vector, i, 1);
            }
        }
    }

    public void updateDebugGroups(String[] groups) {
        debugGroups.clearSelection();

        for (int i = debugGroups.getRowCount() - 1; i >= 0; i--) {
            groupsModel.removeRow(i);
        }

        for (String group : groups) {
            groupsModel.addRow(new Object[]{group});
        }
    }

    public void addListenerToDs(ActionListener a){
        ds.addActionListener(a);
    }
    public void addListenerToRa(ActionListener a){
        ra.addActionListener(a);
    }
    public void addListenerToIncTable(MouseListener e){ incommingTable.addMouseListener(e);}
    public void addListenerToGroupsTable(MouseListener e){debugGroups.addMouseListener(e);}


    public int moveRow(MouseEvent e) {

        Point p = e.getPoint();
        int row = incommingTable.rowAtPoint(p);

        String mem = (String) incommingModel.getValueAt(row, 0);
        String message = (String) incommingModel.getValueAt(row, 1);
        outgoingModel.addRow(new Object[]{mem, message});
        incommingModel.removeRow(row);

        return row;

    }

    public int moveRow() {

        int row = 0;
        String mem = (String) incommingModel.getValueAt(row, 0);
        String message = (String) incommingModel.getValueAt(row, 1);
        outgoingModel.addRow(new Object[]{mem, message});
        incommingModel.removeRow(row);

        return row;

    }

    public int getTableRowCount(){

        return incommingModel.getRowCount();
    }

    public int dropSelected() {
        int row = incommingTable.getSelectedRow();
        if(row != -1) {
            incommingModel.removeRow(row);
        }
        return row;
    }

    public void addListenerToRefresh(ActionListener a) {
        refreshButton.addActionListener(a);
    }

    public String getHost() {
        return host;
    }

    public void setHost() {
        String userInput = JOptionPane.showInputDialog("Enter Host: ", "localhost");

        if (userInput == null) {
            System.exit(0);
        }

        host = userInput;
    }

    public String getGroupName(MouseEvent e) {
        return (String) debugGroups.getValueAt(debugGroups.rowAtPoint(e.getPoint()), 0);
    }

    public void clearDebug() {

        incommingTable.clearSelection();
        for(int i = 0; i < incommingTable.getRowCount(); i++){
            incommingModel.removeRow(0);
        }

        for(int i = 0; i < outgoingTable.getRowCount(); i++){
            outgoingModel.removeRow(0);
        }

        for(int i = 0; i < vectorModel.getRowCount(); i++){
            vectorModel.removeRow(0);
        }
    }
}