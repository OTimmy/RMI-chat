package gcomdebug.Controller;

import gcom.message.*;
import gcom.observer.Observer;
import gcom.observer.ObserverEvent;
import gcom.status.GCOMException;
import gcomdebug.GCOMDebug;
import gcomdebug.view.DebugClient;
import rmi.RMIServer;
import rmi.debugservice.DebugService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Hashtable;

import static javafx.scene.input.KeyCode.T;

/**
 * Created by c12ton on 10/12/16.
 */
public class DebugController {
    private static int num = 0;
    private static String group = null;
    private static DebugClient gui;
    private static GCOMDebug gcom;
    private static DebugService debugService = null;

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


        try {
            debugService = RMIServer.getDebugService(gui.getHost());
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        try {
            debugService.registerControllerObserverMessage(createMessageObserver());
            debugService.registerControllerObserverVector(createVectorObserver());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        String[] groups = gcom.getGroups();
        gui.updateDebugGroups(groups);
    }

    private static gcom.observer.Observer createVectorObserver () throws RemoteException {
        Observer ob = new Observer() {
            @Override
            public <T> void update(ObserverEvent e, T t) throws RemoteException, GCOMException {
                Hashtable<String, Integer> hash = (Hashtable) t;
            }
        };


        return (Observer) UnicastRemoteObject.exportObject(ob,0);
    }

    private static gcom.observer.Observer createMessageObserver() throws RemoteException {
        Observer ob = new Observer() {
            @Override
            public <T> void update(ObserverEvent e, T t) throws RemoteException, GCOMException {
                Message msg = (Message) t;

                if(group.equals(msg.getGroupName())) {
                    switch (msg.getMessageType()) {

                        case CHAT_MESSAGE:
                            Chat chat = (Chat) msg;
                            gui.addIncomming(chat.getUser(), chat.getMessage());
                            break;

                        case LEAVE_MESSAGE:
                            Leave leave = (Leave) msg;
                            gui.addIncomming(leave.getName(), "Leave Message");
                            break;

                        case JOIN_MESSAGE:
                            Join join = (Join) msg;
                            gui.addIncomming(join.getName(), "JOIN Message");
                            break;

                        case ELECTION_MESSAGE:
                            Election election = (Election) msg;
                            gui.addIncomming(election.getLeader().getName(), "Election message");

                            break;
                        default:
                            break;
                    }
                }
            }
        };

        return (Observer) UnicastRemoteObject.exportObject(ob,0);
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
                    group = gui.getGroupName(e);
                    gui.clearDebug();
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
