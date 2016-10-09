package gcom.rmi.rmique;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by c12ton on 2016-10-08.
 * It's used to rebind objects from other computers.
 */
public class RMIServiceConcrete extends UnicastRemoteObject implements RMIService {


    //THIS DOESNT WORK!!! USE hash concurrent!!!!!!!!!
    // save key as group/user!!!!!

    private Registry registry;
    public RMIServiceConcrete(Registry registry) throws RemoteException {
        super();
        this.registry = registry;
    }

    @Override
    public void rebind(String name, Remote obj) throws RemoteException {
        registry.rebind(name,obj);
    }

    @Override
    public Remote lookup(String name) throws RemoteException, NotBoundException {
        return registry.lookup(name);
    }
}
