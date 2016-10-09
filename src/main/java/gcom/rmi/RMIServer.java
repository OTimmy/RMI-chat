package gcom.rmi;

import gcom.rmi.nameservice.NameService;
import gcom.rmi.nameservice.NameServiceData;
import gcom.rmi.rmique.RMIService;
import gcom.rmi.rmique.RMIServiceConcrete;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by c12ton on 2016-10-08.
 * Starts the rmi server, and proved methods to access those instances.
 */
public class RMIServer {

    public static NameService getNameService(String host) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(host);
        return (NameService) registry.lookup(NameService.class.getSimpleName());
    }

    public static RMIService getRMIService(String host) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(host);
        return (RMIService) registry.lookup(RMIService.class.getSimpleName());
    }

    public static void main(String[] args) {
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(1099);

            //Creating name service!
            NameServiceData nameServiceData = new NameServiceData();
            registry.rebind(NameService.class.getSimpleName(), nameServiceData);

            //Creating rmi service
            RMIServiceConcrete rmiServiceConcrete = new RMIServiceConcrete(registry);
            registry.rebind(RMIService.class.getSimpleName(), rmiServiceConcrete);

            System.out.println("Sever is ready!");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
