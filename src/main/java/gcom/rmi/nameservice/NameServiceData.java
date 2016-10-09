package gcom.rmi.nameservice;

import gcom.groupmodule.Member;
import gcom.status.Status;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Created by c12ton on 9/29/16.
 */
public class NameServiceData extends UnicastRemoteObject implements NameService {
    private HashMap<String,Member> leaders;     //Replace with concurrentHashmap

    public NameServiceData() throws RemoteException {
        leaders = new HashMap<String, Member>();
    }
    /**
     *
     * @return
     */
    public synchronized  HashMap getGroups() throws RemoteException{
        return (HashMap<String, Member>) leaders.clone();
    }


    public synchronized Status registerGroup(String groupName, Member member)
            throws RemoteException {

        if(!leaders.containsKey(groupName)) {
            leaders.put(groupName,member);
            return Status.CREATED_GROUP_SUCCESS;
        }

        return Status.GROUP_ALREADY_EXISTS;
    }

    public synchronized Status removeGroup(String groupName) throws RemoteException{
        return null;
    }

//    /**
//     *
//     * @return
//     */
//    public static NameService getNameService(String host)
//            throws RemoteException, NotBoundException {
//
//        Registry registry = LocateRegistry.getRegistry(host);
//        return (NameService) registry.lookup(NameServiceData.class.getSimpleName());
//    }


}
