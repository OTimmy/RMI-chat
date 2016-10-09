package gcom.nameservice;

import gcom.groupmodule.Member;
import gcom.status.Status;

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
        return (ConcurrentHashMap<String, Member>) leaders;
    }

    public synchronized Status registerGroup(String groupName, Member member)
            throws RemoteException {

        if(!leaders.containsKey(groupName)) {
            leaders.put(groupName,member);
            return Status.CREATED_GROUP_SUCCESS;
        }

        return Status.GROUP_ALREADY_EXISTS;
    }

    public void removeGroup(String groupName) throws RemoteException{
        leaders.remove(groupName);

    }

    /**
     *
     * @return
     */
    private static NameService getNameService(String host)
            throws RemoteException, NotBoundException {

        Registry registry = LocateRegistry.getRegistry(host);
        return (NameService) registry.lookup(NameService.class.getSimpleName());
    }

    /**
     *
     * @param host
     * @param groupName
     * @param member
     */
    public static void registerGroup(String host, String groupName,Member member) {
        try {
            NameService nameService = getNameService(host);
            nameService.registerGroup(groupName,member);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

}
