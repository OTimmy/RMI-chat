package gcom.observer;

import gcom.message.Message;
import gcom.status.GCOMException;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 9/29/16.
 */
public interface Observer extends Remote{
    <T>void update(ObserverEvent e,T t) throws RemoteException, GCOMException;
}
