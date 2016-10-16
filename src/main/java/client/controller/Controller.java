package client.controller;

import gcom.AbstractGCOM;
import gcom.message.*;
import gcom.messagemodule.*;
import gcomdebug.GCOMDebug;
import gcomretail.GCOMRetail;
import gcom.groupmodule.GroupProperties;
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

        while (true) {
            try {
                if(gui.getIfDebug()){
                    gcom = new GCOMDebug(gui.getHost());
                }else{
                    gcom = new GCOMRetail(gui.getHost());
                }
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

    private ActionListener createGroupTabListern() {


        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String group = gui.showGroupCreation();

                if (group == null) {
                    return;
                }

                AbstractGCOM gcom = null;

                if (group != null) {

                    try {
                        if(gui.getIfDebug()){
                            gcom = new GCOMDebug(gui.getHost());
                        }else{
                            gcom = new GCOMRetail(gui.getHost());
                        }
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    } catch (NotBoundException e1) {
                        gui.inputHostMessage();
                    }

                    gcom.registerObservers(createMessageObserver(group));
                    GroupProperties p = null;

                    Class order = null;

                    if(gui.getIfNoOrder()){
                        order = UnorderedOrdering.class.getClass();
                    }else{
                        order = CausalOrdering.class.getClass();
                    }

                    try {
                        p = new GroupProperties(gui.getCom(), order, group);
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

                AbstractGCOM gcom = null;

                try {
                    if(gui.getIfDebug()){
                        gcom = new GCOMDebug(gui.getHost());
                    }else{
                        gcom = new GCOMRetail(gui.getHost());
                    }
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
                    gui.showErrorMess("Connection refused: " + e1.getMessage());
                    return;
                }


                gui.addJoinTab(data[1]);
                gui.setMembers(data[1], members);
                gui.addActionListenerSend(data[1], sendListern());

                gui.shiftFocusToTab();
            }
        };
    }

    private ActionListener sendListern() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JButton source = (JButton) e.getSource();
                String group = source.getName();

                String[] data = group.split("/");

                String message = gui.getMessage(data[0]);
                AbstractGCOM gcom = gcomTable.get(data[0]);

                Message msg = null;
                try {
                    msg = new ChatMessage(data[1], message);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                try {
                    gcom.sendMessageToGroup(msg);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }

            }
        };
    }

    private ActionListener refreshListener() {
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

                switch (msg.getMessageType()) {

                    case CHAT_MESSAGE:
                        Chat chat = (Chat) msg;
                        gui.appendMessage(group, chat.getUser() + ": " + chat.getMessage() + "\n\n");
                        break;

                    case LEAVE_MESSAGE:
                        Leave leave = (Leave) msg;
                        gui.removeMember(group, leave.getName());
                        gui.appendMessage(group, leave.getName() + " has left the chat.\n\n");
                        break;

                    case JOIN_MESSAGE:
                        Join join = (Join) msg;
                        gui.appendMessage(group, join.getMember().getName() + " has joined the chat\n\n");
                        gui.addMember(group, join.getMember().getName());
                        break;

                    case ELECTION_MESSAGE:
                        Election election = (Election) msg;

                        if (gui.myNameInGroup(group, election.getLeader().getName())) {
                            gui.setLeaderOf(group);
                        }
                        gui.appendMessage(group, election.getLeader().getName() + " Is now leader.\n\n");
                        break;
                    default:
                        break;
                }
            }
        };

        return ob;
    }
}
