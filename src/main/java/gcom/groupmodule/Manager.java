package gcom.groupmodule;

import gcom.messagemodule.Message;
import gcom.status.GCOMException;

import java.rmi.RemoteException;

/**
 * Created by c12ton on 10/10/16.
 */
public interface Manager {

    String[] getGroups() throws RemoteException;

    Properties getProperties();

    Properties getGroupProperties(String groupName) throws RemoteException;

    Member[] joinGroup(String group, String name) throws GCOMException, RemoteException;

    /**
     * @param groupProp the properties of the group
     * @param name the name of the user
     * @throws RemoteException
     * @throws GCOMException
     */
    void createGroup(Properties groupProp,String name) throws RemoteException,GCOMException;

    void addMember(Member m) throws RemoteException;

    boolean memberExist(Member m) throws RemoteException,GCOMException;

    Member[] getMembers();

    void receivedMessage(Message m);

}
