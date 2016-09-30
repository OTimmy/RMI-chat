package gcom.groupmodule;

import gcom.communicationmodule.Communication;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 9/29/16.
 */
public interface InterfaceMember extends Remote {

    /**
     * @param com is the communcation instance, used to
     *            send message to user
     */
    void setLeaderCom(Communication com) throws RemoteException;

    /**
     * @param membsCom is an array of Communication for an group
     */
    void setMembersCom(Communication[] membsCom) throws RemoteException;

    /**
     *
     * @return
     * @throws RemoteException
     */
    String getGroupName() throws RemoteException;


    /**
     *
     * @param member
     * @throws RemoteException
     */
    void addNewMember(InterfaceMember member) throws RemoteException;

    /**
     *
     * @return
     * @throws RemoteException
     */
    String getName() throws RemoteException;

}
