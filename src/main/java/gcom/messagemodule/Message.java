package gcom.messagemodule;

import gcom.status.GCOMError;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 2016-09-30.
 */
public interface Message extends Remote{
    String getUser() throws RemoteException;
    MessageType getMessageType() throws RemoteException;
    String getChatMessage() throws RemoteException;
}
