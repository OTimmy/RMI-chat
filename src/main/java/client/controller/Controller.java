package client.controller;

import gcom.AbstractGCOM;
import gcomretail.GCOMRetail;
import gcom.groupmodule.GroupProperties;
import gcom.messagemodule.Message;
import gcom.messagemodule.UnorderedMessageOrdering;
import gcom.observer.Observer;
import gcom.observer.ObserverEvent;
import gcom.status.GCOMException;
import client.view.GUIClient;

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
    private static AbstractGCOM gcom;
    private static Hashtable<String, AbstractGCOM> gcomTable = new Hashtable<>();

    private ActionListener createGroupTabListern() {


        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String group = gui.showGroupCreation();

                if(group == null){
                    return;
                }

                AbstractGCOM gcom = null;

                if (group != null) {

                        try {
                            gcom = new GCOMRetail(gui.getHost());
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        } catch (NotBoundException e1) {
                            gui.inputHostMessage();
                        }


                    gcom.registerObservers(createMessageObserver(group));
                    GroupProperties p = null;

                    try {
                        p = new GroupProperties(gui.getCom(), UnorderedMessageOrdering.class.getClass(),group);
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }

                    String username = gui.getName();

                    try {
                        gcom.createGroup(p, username);
                    } catch (GCOMException e1) {
                        e1.printStackTrace();
                    }
                    gui.addGroupTab();
                    gui.setMembers(group, new String[]{username});
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

                GCOMRetail gcom = null;

                try {
                    gcom = new GCOMRetail(gui.getHost());
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                } catch (NotBoundException e1) {
                    e1.printStackTrace();
                }
                gcom.registerObservers(createMessageObserver(data[1]));
                gcomTable.put(data[1], gcom);

                String[] members = null;
                try {
                    members = gcom.connectToGroup(data[1], data[0]);
                } catch (GCOMException e1) {
                    e1.printStackTrace();
                }


                gui.addJoinTab(data[1]);
                gui.setMembers(data[1], members);
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
                AbstractGCOM gcom = gcomTable.get(group);

                gcom.sendMessageToGroup(message);

            }
        };
    }

    private ActionListener refreshListener(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.updateGroups(gcom.getGroups());
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
        gui.addActionListererRefresh(controller.refreshListener());

        while(true) {
            try {
                gcom = new GCOMRetail(gui.getHost());
                break;
            } catch (RemoteException e) {
                gui.setHost();
                continue;
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }

        String[] groups = gcom.getGroups();
        gui.updateGroups(groups);

    }
}
