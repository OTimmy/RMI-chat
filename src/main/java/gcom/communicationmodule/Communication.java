package gcom.communicationmodule;

import gcom.messagemodule.Message;
import gcom.status.GCOMException;
import gcom.status.Status;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 2016-09-30.
 */
public interface Communication {
    Status sendMessage(String[] membersNames, Message message) throws RemoteException, NotBoundException, InterruptedException;
    void waitForMessage() throws RemoteException, InterruptedException;
    Message getMessage() throws RemoteException, GCOMException, InterruptedException;
}
