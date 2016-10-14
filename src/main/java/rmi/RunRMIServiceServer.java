package rmi;

import rmi.debugservice.DebugService;
import rmi.nameservice.NameService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by c12ton on 10/14/16.
 */
public class RunRMIServiceServer extends UnicastRemoteObject implements RMIService{

    public RunRMIServiceServer() throws RemoteException {
    }

    @Override
    public NameService getNameService() throws RemoteException {
        return null;
    }

    @Override
    public DebugService getDebugService() throws RemoteException {
        return null;
    }


    public static void main(String[]args) {

    }
}
