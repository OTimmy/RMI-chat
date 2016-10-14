package gcom.messagemodule;

import gcom.groupmodule.Member;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 10/13/16.
 * This is sent by the node that becomes the new leader of the group
 */
public interface Election extends Remote{

    /**
     * @return The new leader for the group
     */
    Member getLeader() throws RemoteException;

}
