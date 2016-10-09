package gcom.communicationmodule;

import gcom.groupmodule.Member;
import gcom.messagemodule.Message;
import gcom.observer.Observer;
import gcom.status.GCOMException;
import gcom.status.Status;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 2016-09-30.
 */
public interface Communication {
    void sendMessage(Member[] members, Message message) throws RemoteException, NotBoundException, InterruptedException;
    void waitForMessage() throws RemoteException, InterruptedException;
    Message getMessage() throws RemoteException, GCOMException, InterruptedException;
    void putMessage(Message m);
}
