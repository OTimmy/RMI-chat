package gcom.message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 10/13/16.
 */
public interface Leave extends Remote{
    /**
     * @return the name of the member that left the group
     */
    String getName() throws RemoteException;
}
