package controller;

import gcom.GCOM;
import gcom.communicationmodule.NonReliableCommunication;
import gcom.messagemodule.Message;
import gcom.nameservice.NameService;
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
public class Controller{

    private static GUIClient gui;
    private static Hashtable<String, GCOM> gcomTable = new Hashtable<>();

   
    
    private ActionListener createGroupTabListern() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String group = gui.addGroupTab();
                gui.addActionListenerSend(group, sendListern());

                if(group != null){
                    try {
                        GCOM gcom = new GCOM(gui.getHost());
                        gcom.registerObservers(createMessageObserver(group));
                        gcom.createGroup(group, "test",/*TEMP !*/NonReliableCommunication.class.getName());
                        gcomTable.put(group, gcom);
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    } catch (NotBoundException e1) {
                        e1.printStackTrace();
                    } catch (GCOMException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        };
    }

    private ActionListener createDeleteListener(){

        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.deleteGroup();
            }
        };
    }

    private ActionListener createJoinListener(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = gui.joinGroup();

                if(s == null){
                    return;
                }
                String[] data = s.split("/");

                GCOM gcom = gcomTable.get(data[1]);
                try {
                    gcom.connectToGroup(data[1], data[0]);
                }  catch (GCOMException e1) {
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


    private gcom.observer.Observer createMessageObserver(final String group){
        gcom.observer.Observer obs = new gcom.observer.Observer() {

            @Override
            public void update() {
                GCOM gcom = gcomTable.get(group);
                Message msg = null;
                try {
                    msg = gcom.getMessage();
                    gui.appendMessage(group, msg.getUser() + ": \n" +msg.getChatMessage() + "\n\n");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        return obs;
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

        GCOM gcom = null;
        try {
            gcom = new GCOM(gui.getHost());
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        String[] groups = gcom.getGroups();
        gui.updateGroups(groups);

    }
}
