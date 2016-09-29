package gcom.groupmodule;

import gcom.status.Status;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by c12ton on 9/29/16.
 */
public class Group extends UnicastRemoteObject implements GroupInterface{
    private AbstractMember leader;
    private String groupName;

    protected Group() throws RemoteException {
    }


    public Group(String groupName, AbstractMember leader) throws RemoteException {
        super();
        this.groupName = groupName;
        this.leader = leader;

    }


    public Status joinGroup(AbstractMember member) {
        //leader.getMembers
        // for each member
            //member.notifyJoin(member)
        return null;
    }

    public Status setLeader(AbstractMember member) {
        return null;
    }

    public AbstractMember getLeader() {
        return leader;
    }

}
