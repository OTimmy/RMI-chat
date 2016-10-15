package gcom.message;

import gcom.groupmodule.Member;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 10/13/16.
 */
public interface Join extends Remote {
    /**
     * @return the member that joined the group
     */
    Member getMember() throws RemoteException;

    /**
     * @return the name of the member who joined
     * @throws RemoteException
     */
    String getName() throws RemoteException;
}
