package gcom.messagemodule;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 10/13/16.
 * Is used when sending chat messages to each node.
 */
public interface Chat extends Remote{
    /**
     * @return Message from the member
     * @throws RemoteException when unreachable
     */
    String getMessage() throws RemoteException;

    /**
     * @return the senders name
     * @throws RemoteException when unreachable
     */
    String getUser() throws RemoteException;
}
