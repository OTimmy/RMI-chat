package gcom.observer;

import gcom.message.Message;
import gcom.status.GCOMException;

import java.rmi.RemoteException;

/**
 * Created by c12ton on 9/29/16.
 */
public interface Subject {
    void registerObservers(Observer ...obs) throws RemoteException;
    void notifyObserver(ObserverEvent e,Message message) throws RemoteException, GCOMException;
}
