package gcomdebug.communicationmodule;

import gcom.communicationmodule.NonReliableCommunication;
import gcom.message.Message;
import gcom.observer.Observer;
import gcom.observer.ObserverEvent;
import gcom.status.GCOMException;
import rmi.RMIServer;
import rmi.debugservice.DebugService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by c12ton on 10/15/16.
 */
public class NonReliableDebug extends NonReliableCommunication implements Observer{

    private DebugService debugService;
    private boolean isRegistedOnDebug = false;
    private Observer observer;

    private String groupName;
    private String name;

    public NonReliableDebug(String host,String groupName,String name) {
        super();
        this.groupName = groupName;
        this.name = name;
        try {
            debugService = RMIServer.getDebugService(host);
            observer = (Observer) UnicastRemoteObject.exportObject(this,0);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void putMessage(Message m) {
        try {
            if(observer != null) {
                debugService.registerCommunicationObserver(groupName,name,observer);
                isRegistedOnDebug = true;
            }

            debugService.addMessage(m);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(ObserverEvent e, Object arg) throws RemoteException, GCOMException {
        Message m = (Message) arg;
        inMessages.add(m);
    }

    public Observer getObserver() {
        return observer;
    }

    public void setupObserver(String groupName,String name) {

    }
}
