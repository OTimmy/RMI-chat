package gcom.groupmodule;

import gcom.status.Status;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by c12ton on 9/29/16.
 */
public class Group extends UnicastRemoteObject implements GroupInterface{
    private InterfaceMember leader;
    private String groupName;

    protected Group() throws RemoteException {
    }


    public Group(String groupName, InterfaceMember leader) throws RemoteException {
        this.groupName = groupName;
        this.leader = leader;
    }


    public Status joinGroup(InterfaceMember member) {
        //leader.getMembers
        // for each member
            //member.notifyJoin(member)
        return null;
    }

    public Status setLeader(InterfaceMember member) {
        return null;
    }

    public InterfaceMember getLeader() {
        return leader;
    }

}
