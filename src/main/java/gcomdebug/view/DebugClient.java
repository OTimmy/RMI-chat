package gcomdebug.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.RunnableFuture;

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

    /**
     * Init the debug gui
     */
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
        groupsSp.setPreferredSize(new Dimension(200,0));

        refreshButton = new JButton("Refresh");
        refreshButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        g = new JLabel("Currently selected: none");
        g.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
        groupPanel.add(g);
        groupPanel.add(groupsSp);
        groupPanel.add(refreshButton);

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
        outgoingTable.setEnabled(false);
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
        vectorTable.setEnabled(false);
        vectorTable.getTableHeader().setReorderingAllowed(false);
        vectorModel.addColumn("Member");
        JScrollPane scrollvectorPane = new JScrollPane(vectorTable);
        scrollvectorPane.setPreferredSize(new Dimension(1,160));

        JPanel tableBPane = new JPanel();
        tableBPane.setLayout(new BoxLayout(tableBPane, BoxLayout.Y_AXIS));
        tableBPane.add(incOutPane);
        tableBPane.add(buttons);
        tableBPane.add(scrollvectorPane);

        debugPane.add(tableBPane, BorderLayout.WEST);
        debugPane.add(groupPanel, BorderLayout.EAST);

        debugFrame.add(debugPane);
        debugFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        debugFrame.setResizable(false);
        debugFrame.pack();
        debugFrame.setVisible(true);
    }

    //---------------------------------------incomming------------------------------------->

    /**
     * add a message to the incomming window
     * @param from
     * @param to
     * @param message
     */
    public void addIncomming(String from, String to, String message){
        incommingModel.addRow(new Object[]{from, to, message});
    }

    /**
     * remove a certain row from the incoming window
     * @param row
     */
    public void removeFromgIncomming(int row){
        incommingModel.removeRow(row);
    }

    /**
     * get the row index that the click was on
     * @param p
     * @return
     */
    public int getIndexFromPointInc(Point p){
        return incommingTable.rowAtPoint(p);
    }

    /**
     * get how manhy rows the incoming table has
     * @return
     */
    public int getTableRowCount(){
        return incommingModel.getRowCount();
    }

    /**
     * remove a selected row with button
     * @return
     */
    public int dropSelected() {
        int row = incommingTable.getSelectedRow();
        if(row >= 0) {
            incommingModel.removeRow(row);
        }
        return row;
    }

    /**
     * get a valuse from what row was selected
     * @param p
     * @return
     */
    public String getTo(Point p) {
        int row = incommingTable.rowAtPoint(p);
        String s = (String) incommingTable.getValueAt(row, 1);
        return s;
    }

    /**
     * clear the incomming table
     */
    public void clearIncomming() {
        incommingModel.setRowCount(0);
    }

    /**
     * add a listener to the incoming table
     * @param e
     */
    public void addListenerToIncTable(MouseListener e){ incommingTable.addMouseListener(e);}



    //---------------------------------------outgoing------------------------------------->

    /**
     * add to the outgoing table
     * @param from
     * @param to
     * @param message
     */
    public void addOutgoing(String from, String to, String message){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                outgoingModel.addRow(new Object[]{from, to, message});
            }
        });
    }

    /**
     * clear the outgoing table
     */
    public void clearOutGoingTable() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                outgoingModel.setRowCount(0);
            }
        });
    }

    //---------------------------------------Vector------------------------------------->

    /**
     * add a column to the vector table
     * @param member
     */
    public void addColVector(String member){

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if(vectorModel.findColumn(member) < 0)
                    vectorModel.addColumn(member);
            }
        });
    }

    /**
     * add a row or update a current row.
     * @param mem
     * @param vector
     */
    public void addVector(String mem, int[] vector){


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
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
        });
    }

    /**
     * get the names of the columns in the vector table
     * @return
     */
    public String[] getVectorColumns(){
        ArrayList<String> columns = new ArrayList<>();

        for (int i = 0; i < vectorModel.getColumnCount(); i++){
            columns.add(vectorModel.getColumnName(i));
        }
        return columns.toArray(new String[]{});
    }

    /**
     * remove a vector row with its column
     * @param mem
     */
    public void removeVector(String mem){

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < vectorModel.getRowCount(); i++){

                    if(vectorModel.getRowCount()>0){

                        if (vectorModel.getValueAt(i, 0).equals(mem)) {
                            int indexColumn = vectorModel.findColumn(mem);

                            vectorModel.removeRow(i);
                            vectorTable.removeColumn(vectorTable.getColumnModel().getColumn(indexColumn));

                            Vector newV = new Vector();
                            Vector columns = new Vector();

                            ArrayList<Object> list = new ArrayList<>(vectorModel.getDataVector());

                            for(int j = 0; j< list.size(); j++){
                                ((Vector)list.get(j)).remove(indexColumn);
                                newV.addElement(list.get(j));
                            }

                            for(int j = 0; j < vectorTable.getColumnCount(); j++){
                                columns.addElement(vectorTable.getColumnName(j));
                            }

                            vectorModel.setDataVector(newV, columns);
                            break;
                        }
                    }
                }
            }
        });
    }

    //---------------------------------------groups------------------------------------->


    /**
     * update the group list
     * @param groups
     */
    public void updateDebugGroups(String[] groups) {
        debugGroups.clearSelection();

        groupsModel.setRowCount(0);

        for (String group : groups) {
            groupsModel.addRow(new Object[]{group});
        }
    }

    /**
     * get the selected groupname
     * @param e
     * @return
     */
    public String getGroupName(MouseEvent e) {
        String s = (String) debugGroups.getValueAt(debugGroups.rowAtPoint(e.getPoint()), 0);
        return s;
    }

    /**
     * set the selected groupname to lable
     * @param group
     */
    public void addCurrentGroup(String group) {
        g.setText("Currently selected: " + group);
    }

    /**
     * add the listener to the group table
     * @param e
     */
    public void addListenerToGroupsTable(MouseListener e){debugGroups.addMouseListener(e);}

    //---------------------------------------buttons------------------------------------->


    public void addListenerToDs(ActionListener a){
        ds.addActionListener(a);
    }
    public void addListenerToRa(ActionListener a){
        ra.addActionListener(a);
    }
    public void addListenerToRefresh(ActionListener a) {
        refreshButton.addActionListener(a);
    }

    //---------------------------------------Other------------------------------------->

    /**
     * get the host
     * @return
     */
    public String getHost() {
        return host;
    }

    /**
     * set the hostname
     */
    public void setHost() {
        String userInput = JOptionPane.showInputDialog("Enter Host: ", "localhost");

        if (userInput == null) {
            System.exit(0);
        }

        host = userInput;
    }

    /**
     * clear all the windows in the debugger
     */
    public void clearDebug() {

        incommingTable.clearSelection();

        incommingModel.setRowCount(0);
        outgoingModel.setRowCount(0);
        vectorModel.setDataVector(new Object[][]{}, new Object[]{"member"});
    }
}
