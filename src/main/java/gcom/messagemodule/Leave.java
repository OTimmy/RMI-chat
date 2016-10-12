package gcom.messagemodule;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 10/12/16.
 */
public interface Leave extends Remote{
    String getName() throws RemoteException;
}
