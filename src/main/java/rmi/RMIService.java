package rmi;

import rmi.debugservice.DebugService;
import rmi.nameservice.NameService;

import java.rmi.RemoteException;

/**
 * Created by c12ton on 10/14/16.
 */
public interface RMIService {

    NameService getNameService() throws RemoteException;
//
    DebugService getDebugService() throws RemoteException;
}
