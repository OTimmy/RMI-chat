package gcom.rmi.nameservice;

import gcom.groupmodule.Member;
import gcom.status.Status;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Created by c12ton on 2016-09-30.
 */
public interface NameService extends Remote {
    HashMap getGroups() throws RemoteException;
    Status registerGroup(String groupName, Member member) throws RemoteException;
    Status removeGroup(String groupName) throws RemoteException;
}
