package gcom.messagemodule;

import gcom.status.Status;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 2016-09-30.
 */
public interface Message extends Remote{
    String getUser() throws RemoteException;
    Status getStatusMessage() throws RemoteException;
    String getChatMessage() throws RemoteException;
}
