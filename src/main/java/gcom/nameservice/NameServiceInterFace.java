package gcom.nameservice;

import gcom.groupmodule.InterfaceMember;
import gcom.status.Status;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 2016-09-30.
 */
public interface NameServiceInterFace extends Remote {
    InterfaceMember[] getGroups() throws RemoteException;
    Status registerGroup(String groupName, InterfaceMember member) throws RemoteException;
    Status removeGroup(String groupName) throws RemoteException;
}
