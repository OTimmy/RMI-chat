package rmi.debugservice;

import gcom.message.Message;
import gcom.observer.Observer;
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
    void addMessage(Message m) throws RemoteException;

    /**
     * @param groupName
     * @param index
     */
    void passMessage(String groupName,int index) throws GCOMException, RemoteException;

    /**
     *
     * @param groupName
     * @throws GCOMException
     * @throws RemoteException
     */
    void passMessages(String groupName) throws GCOMException,RemoteException;

    /**
     * @param name
     * @param vectorClock
     */
    void updateVectorClock(String name, HashMap<String,Integer> vectorClock) throws RemoteException;



    /**
     * @param groupName
     * @param b
     */
    void registerCommunicationObserver(String groupName,String name, Observer b) throws RemoteException;

    /**
     * @param b
     */
    void registerControllerObserverMessage(Observer b) throws RemoteException;

    void registerControllerObserverVector(Observer b) throws RemoteException;
}
