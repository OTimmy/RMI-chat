package gcomdebug.Controller;

import gcomdebug.GCOMDebug;
import gcomdebug.view.DebugClient;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 10/12/16.
 */
public class DebugController {
    private static int num = 0;
    private static DebugClient gui;
    private static GCOMDebug gcom;

    public DebugController(DebugClient gui) {
        this.gui = gui;
    }

    public static void main(String[] args) {

        DebugController controller;

        controller = new DebugController(new DebugClient());

        gui.addListenerToRa(createActionlistenerRa());
        gui.addListenerToDs(createActionlistenerDs());
        gui.addListenerToRefresh(createSctionListenerRefresh());
        gui.addListenerToIncTable(createIncommingTableListener());
        gui.addListenerToGroupsTable(createGroupsTableListener());


        while (true) {
            try {
                gcom = new GCOMDebug(gui.getHost());
                break;
            } catch (RemoteException e) {
                gui.setHost();
                continue;
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }

        String[] groups = gcom.getGroups();
        gui.updateDebugGroups(groups);
    }

    private static ActionListener createActionlistenerRa() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int countRows = gui.getTableRowCount();
                for (int i = 0; i < countRows; i++) {
                    int index = gui.moveRow();
                    //do something with index!
                }
            }
        };
    }

    private static ActionListener createActionlistenerDs() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int index = gui.dropSelected();
                //do something with index!!
            }
        };
    }

    private static ActionListener createSctionListenerRefresh() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.addIncomming("test", "message" + num);
                num++;

                gui.updateDebugGroups(gcom.getGroups());
            }
        };
    }

    private static MouseListener createIncommingTableListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {
                    int index = gui.moveRow(e);
                    //do something with index!
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        };
    }

    private static MouseListener createGroupsTableListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {
                    //get the groupname etc.

                    System.out.println("JOIN GROUP NOT IMPLEMENTED YET");
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        };
    }
}
