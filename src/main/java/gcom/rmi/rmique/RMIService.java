package gcom.rmi.rmique;

import gcom.communicationmodule.QueCommunication;
import gcom.communicationmodule.QueCommunicationRMI;
import gcom.rmi.nameservice.NameService;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 2016-10-08.
 */
public interface RMIService extends Remote{
    void rebind(String name, Remote obj) throws RemoteException;
    Remote lookup(String name) throws RemoteException, NotBoundException;
}
