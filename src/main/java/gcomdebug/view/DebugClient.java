package gcomdebug.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Created by c13slk on 2016-10-14.
 */
public class DebugClient {

    private JFrame debugFrame = new JFrame("Debug");

    public DebugClient() {

        DefaultTableModel groupsModel = new DefaultTableModel();
        DefaultTableModel messModel = new DefaultTableModel();
        DefaultTableModel vectorModel = new DefaultTableModel();

        JPanel debugPane = new JPanel(new BorderLayout());

        JTable debugGroups = new JTable(groupsModel) {
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

        JTable messTable = new JTable(messModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        messModel.addColumn("Member");
        messModel.addColumn("Message");
        JScrollPane scrollMessPane = new JScrollPane(messTable);

        JTable vectorTable = new JTable(vectorModel) {
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
}
