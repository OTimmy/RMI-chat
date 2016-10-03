package gcom.nameservice;

import gcom.groupmodule.Member;
import gcom.status.Status;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 2016-09-30.
 */
public interface NameServiceInterFace extends Remote {
    Member[] getGroups() throws RemoteException;
    Status registerGroup(String groupName, Member member) throws RemoteException;
    Status removeGroup(String groupName) throws RemoteException;
}
