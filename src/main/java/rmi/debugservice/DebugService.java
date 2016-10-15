package rmi.debugservice;

import gcom.message.Message;
import gcom.observer.Observer;
import gcom.observer.Subject;
import gcom.status.GCOMException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Created by c12ton on 10/14/16.
 */
public interface DebugService extends Remote {

    /**
     * @param m
     */
    void addMessage(Message m);

    /**
     * @param groupName
     * @param index
     */
    void passMessage(String groupName,int index) throws GCOMException, RemoteException;

    /**
     * @param name
     * @param vectorClock
     */
    void updateVectorClock(String name, HashMap<String,Integer> vectorClock);



    /**
     * @param b
     */
    void registerCommunicationObserver(Observer b);

    /**
     * @param b
     */
    void registerControllerObserverMessage(Observer b);

    void registerControllerObserverVector(Observer b);
}
