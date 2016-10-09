package gcom.nameservice;

import gcom.groupmodule.Member;
import gcom.status.Status;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by c12ton on 2016-09-30.
 */
public interface NameService extends Remote {
    ConcurrentHashMap<String, Member> getGroups() throws RemoteException;
    void registerGroup(String groupName, Member member) throws RemoteException;
    void removeGroup(String groupName) throws RemoteException;
}
