package gcom.observer;

import java.rmi.RemoteException;

/**
 * Created by c12ton on 9/29/16.
 */
public interface Subject {
    void registerObservers(Observer ...obs) throws RemoteException;
    void notifyObserver() throws RemoteException;
}
