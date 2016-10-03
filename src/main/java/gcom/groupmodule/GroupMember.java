package gcom.groupmodule;

import gcom.messagemodule.Message;
import gcom.status.Status;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;

/**
 * Created by c12ton on 9/29/16.
 */
public class GroupMember extends UnicastRemoteObject implements Member {
    private String id;
    private String groupName;
    private HashSet<Member> members;
    private Member leader;

    protected GroupMember(String id) throws RemoteException {
        this.id = id;
        this.groupName = groupName;
    }


    public Status sendMessage(Message m) {
        return null;
    }

    public String getID() throws RemoteException {
        return null;
    }

    public Class getCommunicationType() throws RemoteException {
        return null;
    }


    //AskForbeingLeader()
        //algorithm

    //registerGroup()
        //nameService = NameService()
        // nameService.reregisterGroup(
}
