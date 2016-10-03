package gcom.nameservice;

import gcom.groupmodule.GroupLeader;
import gcom.groupmodule.Leader;
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
public class NameService extends UnicastRemoteObject implements  NameServiceInterFace{
    private HashMap<String,Leader> leaders;

    protected NameService() throws RemoteException {
        leaders = new HashMap<String,Leader>();
    }

    /**
     *
     * @return
     */
    public synchronized  Member[] getGroups() throws RemoteException{
        ArrayList<Member> leaders = new ArrayList<Member>();
        for(String key:groups.keySet()) {
            leaders.add(groups.get(key));
        }

        return leaders.toArray(new Member[]{});
    }


    public synchronized Status registerGroup(String groupName, Member member)
            throws RemoteException {

        if(!groups.containsKey(groupName)) {
            groups.put(groupName,member);
            return Status.CREATED_GROUP_SUCCESS;
        }

        return Status.GROUP_ALREADY_EXISTS;
    }

    public synchronized Status removeGroup(String groupName) throws RemoteException{
        return null;
    }

    /**
     *
     * @return
     */
    public static NameServiceInterFace getNameService(String host)
            throws RemoteException, NotBoundException {

        Registry registry = LocateRegistry.getRegistry(host);
        return (NameServiceInterFace) registry.lookup(NameService.class.getSimpleName());
    }


    public static void main(String[] args) {
        startService();
    }

    private static void startService() {

        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            //Initiate an empty hashet of groups
            NameService nameService = new NameService();
            registry.rebind(NameService.class.getSimpleName(), nameService);
            System.out.println("Sever is ready!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
