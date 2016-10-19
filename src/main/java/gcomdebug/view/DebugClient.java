package gcomdebug.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

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
    private JTable vectorTable;

    private JLabel g;

    public DebugClient() {

        setHost();

        JPanel debugPane = new JPanel(new BorderLayout());

        debugGroups = new JTable(groupsModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        groupsModel.addColumn("Groups");
        JScrollPane groupsSp = new JScrollPane(debugGroups);

        refreshButton = new JButton("Refresh");
        refreshButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        g = new JLabel("Currently selected: none");
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

        incommingTable.getTableHeader().setReorderingAllowed(false);
        incommingModel.addColumn("From");
        incommingModel.addColumn("To");
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
        outgoingTable.getTableHeader().setReorderingAllowed(false);
        outgoingModel.addColumn("From");
        outgoingModel.addColumn("To");
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


        vectorTable = new JTable(vectorModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vectorTable.setEnabled(false);//------------------------------------------------------------->
        vectorTable.getTableHeader().setReorderingAllowed(false);
        vectorModel.addColumn("Member");
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

    public void addIncomming(String from, String to, String message){
        incommingModel.addRow(new Object[]{from, to, message});

    }

    public void removeFromgIncomming(int row){
        incommingModel.removeRow(row);
    }

    public int getIndexFromPointInc(Point p){
        return incommingTable.rowAtPoint(p);
    }

    public void addOutgoing(String from, String to, String message){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                outgoingModel.addRow(new Object[]{from, to, message});
            }
        });
    }

    public void addColVector(String member){
        if(vectorModel.findColumn(member) == -1){
            vectorModel.addColumn(member);
        }
    }

    //---------------------------------------------------------------------------------------------------------------->

    public void addVector(String mem, int[] vector){

        Object[] ob = new Object[vector.length+1];
        ob[0] = mem;

        for(int i = 0; i < vector.length; i++){
            ob[i+1] = vector[i];

        }

        int index = -1;
        int row = vectorModel.getRowCount();

        for(int i = 0; i < row; i++){
            if(vectorModel.getValueAt(i,0).equals(mem)){
                index = i;
            }
        }

        if(index != -1){
            for(int i = 1; i< ob.length;i++){
                vectorModel.setValueAt(ob[i],index,i);
            }

        }else {
            vectorModel.addRow(ob);
        }
    }

    //---------------------------------------------------------------------------------------------------------------->

    public String[] getVectorColumns(){
        ArrayList<String> columns = new ArrayList<>();

        for (int i = 0; i < vectorModel.getColumnCount(); i++){
            columns.add(vectorModel.getColumnName(i));
        }
        return columns.toArray(new String[]{});
    }

    public void removeVector(String mem){
        int rows = vectorModel.getRowCount();
        for(int i = 0; i < rows ; i++){

            if (vectorModel.getValueAt(i, 0).equals(mem)) {
                vectorModel.removeRow(i);
                vectorTable.removeColumn(vectorTable.getColumnModel().getColumn(i));
                i--;
            }
        }
    }

    public void updateDebugGroups(String[] groups) {
        debugGroups.clearSelection();

        groupsModel.setRowCount(0);

        for (String group : groups) {
            System.out.println(group);
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

    public int getTableRowCount(){

        return incommingModel.getRowCount();
    }

    public int dropSelected() {
        int row = incommingTable.getSelectedRow();
        if(row >= 0) {
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
        String s = (String) debugGroups.getValueAt(debugGroups.rowAtPoint(e.getPoint()), 0);
        return s;
    }

    public void clearDebug() {

        incommingTable.clearSelection();

        incommingModel.setRowCount(0);
        outgoingModel.setRowCount(0);
        vectorModel.setRowCount(0);
    }

    public void addCurrentGroup(String group) {

        g.setText("Currently selected: " + group);
    }

    public String getTo(Point p) {
        int row = incommingTable.rowAtPoint(p);
        String s = (String) incommingTable.getValueAt(row, 1);
        return s;
    }


    public void clearOutGoingTable() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                outgoingModel.setRowCount(0);
            }
        });
    }

    public void clearIncomming() {
        incommingModel.setRowCount(0);
    }
}
