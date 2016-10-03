package gcom.groupmodule;

import gcom.communicationmodule.Communication;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 9/29/16.
 */
public interface Member extends Remote {

    /**
     * Returns members id
     * @return
     * @throws RemoteException
     */
    String getID() throws RemoteException;

}
