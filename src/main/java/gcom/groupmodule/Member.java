package gcom.groupmodule;

import gcom.message.Message;
import gcom.status.GCOMException;

import java.rmi.Remote;
import java.rmi.RemoteException;

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
    void requestToJoin(Member m) throws RemoteException, GCOMException;

    /**
     * Set members for a member
     */
    void setMember(Member ...m) throws RemoteException;

    /**
     * @return the properties of the group
     * @throws RemoteException
     */
    Properties getProperties() throws RemoteException;

    /**
     * @param m
     * @throws RemoteException
     */
    void sendMessage(Message m) throws RemoteException;

}
