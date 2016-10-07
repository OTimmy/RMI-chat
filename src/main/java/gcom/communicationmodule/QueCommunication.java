package gcom.communicationmodule;

import gcom.messagemodule.Message;
import gcom.status.GCOMException;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 2016-10-06.
 */
public interface QueCommunication extends Remote {
    void putChatMessage(Message m) throws RemoteException, InterruptedException;
    void putErrMessage(String err) throws RemoteException;
    void waitForChatMessage() throws RemoteException, InterruptedException;
    Message getMessage() throws RemoteException, GCOMException, InterruptedException;

}
