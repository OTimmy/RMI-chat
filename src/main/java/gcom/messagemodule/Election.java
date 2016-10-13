package gcom.messagemodule;

import gcom.groupmodule.Member;

import java.rmi.Remote;

/**
 * Created by c12ton on 10/13/16.
 * This is sent by the node that becomes the new leader of the group
 */
public interface Election extends Remote{

    /**
     * @return The new leader for the group
     */
    Member getLeader();

}
