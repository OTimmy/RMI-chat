package gcom.groupmodule;

import gcom.message.Message;
import gcom.status.GCOMException;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 10/10/16.
 */
public interface Manager extends Remote{

    String[] getGroups() throws RemoteException;

    /**
     * @return the properties for the group
     */
    Properties getProperties();

    /**
     * @param groupName the name of the group
     * @return the properties of the group by the given name
     * @throws RemoteException
     */
    Properties getGroupProperties(String groupName) throws RemoteException;

    /**
     * Join a group in gcom
     * @param group the name of the group
     * @param name the of name the user
     * @return A list of current active members
     * @throws GCOMException in case the name already exists
     * @throws RemoteException
     */
    Member[] joinGroup(Properties properties, String name) throws GCOMException, RemoteException;

    /**
     * @param groupProp the properties of the group
     * @param name the name of the user
     * @throws RemoteException
     * @throws GCOMException
     */
    void createGroup(Properties groupProp,String name) throws RemoteException,GCOMException;

    void addMember(Member m) throws RemoteException;

    void removeMember(String name) throws RemoteException;

    /**
     * Loop trough all members, and only return list of working members
     * @return a list of current active members in group
     * @throws RemoteException
     */
    Member[] getMembers() throws RemoteException;

    /**
     * @param m message to be received from a member in the group
     */
    void receivedMessage(Message m);

    boolean electLeader(String groupName,Member m) throws RemoteException;

    void setLeader(Member m);

    String getName();

    /**
     * Is called when member want's to join current group.
     * @param m
     * @throws RemoteException
     */
    void leaderMemberJoin(Member m) throws RemoteException, GCOMException;

    /**
     *  Delets the grom from name service, and sends delete message to group
     */
    void leaderDeleteGroup();

}
