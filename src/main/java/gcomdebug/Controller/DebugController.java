package gcomdebug.Controller;

import gcom.message.*;
import gcom.observer.Observer;
import gcom.observer.ObserverEvent;
import gcom.status.GCOMException;
import gcomdebug.GCOMDebug;
import gcomdebug.view.DebugClient;
import rmi.RMIServer;
import rmi.debugservice.DebugService;
import rmi.debugservice.DelayContainer;
import rmi.debugservice.VectorContainer;
import rmi.debugservice.VectorData;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;


/**
 * Created by c12ton on 10/12/16.
 */
public class DebugController {
    private static String group = "";
    private static DebugClient gui;
    private static GCOMDebug gcom;
    private static DebugService debugService = null;

    private  static final Object lock = new Object();

    public DebugController(DebugClient gui) {
        DebugController.gui = gui;
    }

    public static void main(String[] args) {

        new DebugController(new DebugClient());

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
            debugService.registerControllerOutGoingMessage(createDelayObserver());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        String[] groups = gcom.getGroups();
        gui.updateDebugGroups(groups);
    }

    /**
     * observer for the vector, when its time to update
     * @return  observer
     * @throws RemoteException
     */
    private static gcom.observer.Observer createVectorObserver () throws RemoteException {
        Observer ob = new Observer() {
            @Override
            public <T> void update(ObserverEvent e, T t) throws RemoteException, GCOMException {

                if (t != null){
                    VectorContainer data = (VectorContainer) t;

                    if(group.equals(data.getGroupName())) {
                        HashMap<String, Integer> hashTable = data.getVectorClock();

                        String[] columns = gui.getVectorColumns();
                        int[] values = new int[columns.length-1];

                        for (int i = 1; i < columns.length; i++) {

                            if (hashTable.get(columns[i]) == null) {
                                values[i-1] = 0;
                            } else {
                                values[i-1] = hashTable.get(columns[i]);
                            }
                        }
                        String first = data.getName();

                        gui.addVector(first, values);
                    }
                }
            }
        };

        return (Observer) UnicastRemoteObject.exportObject(ob,0);
    }

    /**
     * observer for when to update the outgoing table
     * @return observer
     * @throws RemoteException
     */
    private static gcom.observer.Observer createDelayObserver() throws RemoteException{
        Observer ob = new Observer() {
            @Override
            public <T> void update(ObserverEvent e, T t) throws RemoteException, GCOMException {

                synchronized (lock) {
                    ArrayList<Message> messages = (ArrayList<Message>) t;

                    gui.clearOutGoingTable();
                    for (Message delay : messages) {
                        if (group.equals(delay.getGroupName())) {
                            String messageText = "";
                            switch (delay.getMessageType()) {
                                case CHAT_MESSAGE:
                                    messageText = ((Chat)delay).getMessage();
                                    break;
                                default:
                                    messageText = delay.getMessageType().toString();
                                    break;
                            }


                            Integer[] hash = delay.getVectorClock().values().toArray(new Integer[]{});

                            StringBuilder strBuilder = new StringBuilder();
                            for (int i = 0; i < hash.length; i++) {
                                strBuilder.append(hash[i]+",");
                            }
                            String newString = "  [" + strBuilder.toString();
                            newString = newString.substring(0,newString.length()-1) + "]";

                            messageText += newString;

                            gui.addOutgoing(delay.getFromName(), delay.getToName(), messageText);
                        }
                    }
                }
            }
        };

        return (Observer) UnicastRemoteObject.exportObject(ob,0);
    }

    /**
     * Observer for when a message has arrived
     * @return  observer
     * @throws RemoteException
     */
    private static gcom.observer.Observer createMessageObserver() throws RemoteException {
        Observer ob = new Observer() {
            @Override
            public <T> void update(ObserverEvent e, T t) throws RemoteException, GCOMException {
                Message msg = (Message) t;

                if(group.equals(msg.getGroupName())) {
                    switch (msg.getMessageType()) {

                        case CHAT_MESSAGE:

                            String messageString = ((Chat) msg).getMessage();

                            Integer[] hash = msg.getVectorClock().values().toArray(new Integer[]{});

                            StringBuilder strBuilder = new StringBuilder();
                            for (int i = 0; i < hash.length; i++) {
                                strBuilder.append(hash[i]+",");
                            }
                            String newString = "  [" + strBuilder.toString();
                            newString = newString.substring(0,newString.length()-1) + "]";

                            messageString += newString;

                            gui.addIncomming(msg.getFromName(), msg.getToName(), messageString );
                            break;

                        case LEAVE_MESSAGE:
                            gui.addIncomming(msg.getFromName(), msg.getToName(), msg.getMessageType().toString());
                            gui.removeVector(((Leave)msg).getName());
                            break;

                        case JOIN_MESSAGE:
                            gui.addIncomming(msg.getFromName(), msg.getToName(), msg.getMessageType().toString());
                            gui.addColVector(((Join)msg).getName());
                            break;

                        case ELECTION_MESSAGE:
                            gui.addIncomming(msg.getFromName(), msg.getToName(), msg.getMessageType().toString());

                            break;
                        case DELETE_MESSAGE:
                            gui.addIncomming(msg.getFromName(), msg.getToName(), msg.getMessageType().toString());
                            break;
                        default:
                            break;
                    }
                }else{
                    debugService.passMessages(msg.getGroupName());
                }
            }
        };

        return (Observer) UnicastRemoteObject.exportObject(ob,0);
    }

    /**
     * actionlistener when pressing the release all button
     * @return
     */
    private static ActionListener createActionlistenerRa() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int countRows = gui.getTableRowCount();
                for (int i = 0; i < countRows; i++) {
                    try {
                        debugService.passMessages(group);
                    } catch (GCOMException e1) {
                    } catch (RemoteException e1) {
                    }
                }

                if(gui.getTableRowCount() > 0){
                    try {
                        debugService.passMessages(group);
                    } catch (GCOMException e1) {
                    } catch (RemoteException e1) {
                    }
                }

                gui.clearIncomming();
            }
        };
    }

    /**
     * actionlistener when pressing the drop selected button
     * @return
     */
    private static ActionListener createActionlistenerDs() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int index = gui.dropSelected();
                try {
                    debugService.dropMessage(group, index);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }
        };
    }

    /**
     * actionlistener when pressing the refresh button
     * @return
     */
    private static ActionListener createSctionListenerRefresh() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.updateDebugGroups(gcom.getGroups());
            }
        };
    }

    private static MouseListener createIncommingTableListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {


                if (e.getClickCount() >= 2) {
                    Point p = e.getPoint();
                    int index = gui.getIndexFromPointInc(p);

                    try {
                        debugService.passMessage(group, gui.getTo(p), index);
                    } catch (GCOMException e1) {
                        e1.printStackTrace();
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                    gui.removeFromgIncomming(index);
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

                if (e.getClickCount() >= 2) {

                    if(!group.equals(gui.getGroupName(e))) {
                        gui.clearIncomming();

                        try {
                            debugService.passMessages(group);
                        } catch (GCOMException e1) {
                            e1.printStackTrace();

                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                        group = gui.getGroupName(e);
                        gui.addCurrentGroup(group);
                        gui.clearDebug();

                        String[] members = new String[0];
                        try {
                            members = debugService.getMemberOfGroups(gui.getGroupName(e));
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }

                        for (String mem :members) {
                            gui.addColVector(mem);
                        }
                    }
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
