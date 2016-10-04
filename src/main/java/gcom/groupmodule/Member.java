package gcom.groupmodule;

import gcom.communicationmodule.Communication;
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
     *
     * @
     */
    Status joinGroup(Member m) throws RemoteException;

    /**
     * Sends message to group that there's no member left.
     */
    void removeGroup() throws RemoteException;

    /**
     * Set members for a member
     */
    void setMembers(HashMap<String,Member> members) throws RemoteException;


    /**
     *
     * @return
     * @throws RemoteException
     */
    String getCommunicationType() throws RemoteException;



}
