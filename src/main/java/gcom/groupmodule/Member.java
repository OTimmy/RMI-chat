package gcom.groupmodule;

import gcom.communicationmodule.Communication;
import gcom.messagemodule.Message;
import gcom.observer.Observer;
import gcom.status.GCOMException;
import gcom.status.Status;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Created by c12ton on 9/29/16.
 */
public interface Member extends Remote {

    /**
     * Returns members id
     * @return
     * @throws RemoteException
     */
    String getName() throws RemoteException;


    /**
     * @param m member to joing group
     */
    void joinGroup(Member m) throws RemoteException, GCOMException;

    /**
     * Sends message to group that there's no member left.
     */
    void removeGroup() throws RemoteException;

    /**
     * Creats a group by given name. And already known
     * communcation type.
     */
    void createGroup(String groupName) throws RemoteException;

    /**
     * Set members for a member
     */
    void setMembers(Member[] m) throws RemoteException;

    /**
     *
     * @return
     * @throws RemoteException
     */
    String getCommunicationType() throws RemoteException;

    void sendMessageToMember(Message m);

}
