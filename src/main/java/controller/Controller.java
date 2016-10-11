package controller;

import gcom.GCOM;
import gcom.communicationmodule.NonReliableCommunication;
import gcom.groupmodule.GroupProperties;
import gcom.messagemodule.Message;
import gcom.messagemodule.UnorderedMessageOrdering;
import gcom.observer.Observer;
import gcom.observer.ObserverEvent;
import gcom.status.GCOMException;
import view.GUIClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Hashtable;


/**
 * Created by c12ton on 2016-10-06.
 */
public class Controller {

    private static GUIClient gui;
    private static Hashtable<String, GCOM> gcomTable = new Hashtable<>();

    private ActionListener createGroupTabListern() {


        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String group = gui.showGroupCreation();

                GCOM gcom = null;

                if (group != null) {
                    while(true) {
                        try {
                            gcom = new GCOM(gui.getHost());
                            break;
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        } catch (NotBoundException e1) {
                            gui.inputHostMessage();
                        }
                    }

                    gcom.registerObservers(createMessageObserver(group));
                    GroupProperties p = null;

                    try {
                        p = new GroupProperties(gui.getCom(), UnorderedMessageOrdering.class.getClass(),group);
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }

                    try {
                        gcom.createGroup(p, gui.getName());
                    } catch (GCOMException e1) {
                        e1.printStackTrace();
                    }
                    gui.addGroupTab();
                    gui.addActionListenerSend(group, sendListern());
                    gcomTable.put(group, gcom);
                }
            }
        };
    }

    private ActionListener createDeleteListener() {

        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.deleteGroup();
            }
        };
    }

    private ActionListener createJoinListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = gui.joinGroup();

                if (s == null) {
                    return;
                }
                String[] data = s.split("/");

                GCOM gcom = null;

                try {
                    gcom = new GCOM(gui.getHost());
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                } catch (NotBoundException e1) {
                    e1.printStackTrace();
                }
                gcom.registerObservers(createMessageObserver(data[1]));
                gcomTable.put(data[1], gcom);


                try {
                    gcom.connectToGroup(data[1], data[0]);
                } catch (GCOMException e1) {
                    e1.printStackTrace();
                }

                gui.addJoinTab(data[1]);
                gui.addActionListenerSend(data[1], sendListern());

            }
        };
    }

    private ActionListener sendListern() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JButton source = (JButton) e.getSource();
                String group = source.getName();
                String message = gui.getMessage(group);
                GCOM gcom = gcomTable.get(group);

                gcom.sendMessageToGroup(message);

            }
        };
    }

    public Controller(GUIClient gui) throws RemoteException, NotBoundException {
        this.gui = gui;
    }


    private gcom.observer.Observer createMessageObserver(final String group) {
        Observer ob = new Observer() {
            @Override
            public <T> void update(ObserverEvent e, T t) throws RemoteException, GCOMException {
                Message msg = (Message) t;
                System.out.println(msg.getChatMessage());
                gui.appendMessage(group, msg.getUser() + ": \n" + msg.getChatMessage() + "\n\n");
            }
        };

        return ob;
    }


    public static void main(String[] args) {

        Controller controller = null;

        try {
            controller = new Controller(new GUIClient());
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        gui.addActionListenerCreate(controller.createGroupTabListern());
        gui.addActionListenerDelete(controller.createDeleteListener());
        gui.addActionListenerJoin(controller.createJoinListener());

        GCOM gcom;
        while(true){
            try {
                gcom = new GCOM(gui.getHost());
                break;
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                gui.inputHostMessage();
            }
        }

        String[] groups = gcom.getGroups();
        gui.updateGroups(groups);

    }
}
