package gcom.groupmodule;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 10/10/16.
 */
public interface Properties extends Remote {
    String getComtype() throws RemoteException;
    String getMessagetype() throws RemoteException;
    String getGroupName() throws RemoteException;
}
