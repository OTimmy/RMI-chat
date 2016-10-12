package gcom.observer;

import gcom.messagemodule.Message;
import gcom.status.GCOMException;

import java.rmi.RemoteException;

/**
 * Created by c12ton on 9/29/16.
 */
public interface Observer {
    void update(ObserverEvent e,Message t) throws RemoteException, GCOMException;
}
