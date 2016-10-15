package rmi;

import rmi.debugservice.DebugService;
import rmi.debugservice.DebugServiceConcrete;
import rmi.nameservice.NameService;
import rmi.nameservice.NameServiceConcrete;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by c12ton on 10/14/16.
 */
public class RMIServer {


    /**
     * @param host where the debug is stored.
     * @return debug instance by gven host
     * @throws RemoteException
     * @throws NotBoundException
     */
    public static DebugService getDebugService(String host) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(host);
        return (DebugService) registry.lookup(DebugService.class.getSimpleName());
    }

    /**
     * @param host where the nameservice is stored
     * @return name service instance by given host
     * @throws RemoteException
     * @throws NotBoundException
     */
    public static NameService getNameService(String host) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(host);
        return (NameService) registry.lookup(host);
    }

    public static void main(String[]args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);

            DebugService debugService = new DebugServiceConcrete();
            registry.rebind(DebugService.class.getSimpleName(),debugService);

            NameService nameService = new NameServiceConcrete();
            registry.rebind(NameService.class.getSimpleName(),nameService);

            System.err.println("Server is ready!");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
