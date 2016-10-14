package rmi.nameservice;

import gcom.groupmodule.Member;
import gcom.status.GCOMError;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by c12ton on 9/29/16.
 */
public class NameServiceConcrete extends UnicastRemoteObject implements NameService {
    private ConcurrentHashMap<String,Member> leaders;
    public NameServiceConcrete() throws RemoteException {
        leaders = new ConcurrentHashMap();
    }

    /**
     * @return current groups
     */
    public synchronized ConcurrentHashMap<String, Member> getGroups() throws RemoteException{
        return  leaders;
    }

    @Override
    public GCOMError registerGroup(String groupName, Member member)
            throws RemoteException {

        if(!leaders.containsKey(groupName)) {
            leaders.put(groupName,member);
            return GCOMError.CREATED_GROUP_SUCCESS;
        }

        return GCOMError.GROUP_ALREADY_EXISTS;
    }

    @Override
    public void removeGroup(String groupName) throws RemoteException{
        leaders.remove(groupName);

    }

    @Override
    public synchronized boolean replaceLeader(String groupName, Member m) throws RemoteException {
        try{
            leaders.get(groupName).getName();
        } catch (Exception e) {
            leaders.put(groupName,m);
            return  true;
        }
        return false;
    }

    @Override
    public Member getGroupLeader(String groupName) {
        return leaders.get(groupName);
    }

    /**
     *
     * @returnString[]
     */
    public static NameService getNameService(String host)
            throws RemoteException, NotBoundException {

        Registry registry = LocateRegistry.getRegistry(host);
        return (NameService) registry.lookup(NameService.class.getSimpleName());
    }


    public static void main(String[] args) {
        startService();
    }

    private static void startService() {

        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            //Initiate an empty hashet of groups
            NameService nameService = new NameServiceConcrete();
            registry.rebind(NameService.class.getSimpleName(), nameService);
            System.out.println("Sever is ready!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
