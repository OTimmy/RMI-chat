package controller;

import gcom.GCOM;
import gcom.messagemodule.Message;
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

//                if(group != null){
//                    try {
//                        GCOM gcom = new GCOM(null);
//                        gcom.registerObservers(createMessageObserver(group));
//
//                        gcomTable.put(group, gcom);
//                    } catch (RemoteException e1) {
//                        e1.printStackTrace();
//                    } catch (NotBoundException e1) {
//                        e1.printStackTrace();
//                    }
//                }
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
                gui.joinGroup();
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
                System.out.println(message);

            }
        };
    }

    public Controller(GUIClient gui) throws RemoteException, NotBoundException {
        this.gui = gui;
    }

    public void getMessageFromGCOM (String group){

    }

    private gcom.observer.Observer createMessageObserver(final String group){
        gcom.observer.Observer obs = new gcom.observer.Observer() {

            @Override
            public void update() {
                GCOM gcom = gcomTable.get(group);
                //Message msg = gcom.getChatMessages();
                //gui.appendMessage(group, msg.getChatMessage);
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

    }




}
