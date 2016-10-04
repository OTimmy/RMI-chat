package gcom.groupmodule;

import gcom.messagemodule.Message;
import gcom.status.Status;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * Created by c12ton on 9/29/16.
 */
public class GroupMember extends UnicastRemoteObject implements Member {
    private String name;
    private String groupName;
    private HashMap<String,Member> members;


    private Member leader;

    protected GroupMember(String name, String groupName) throws RemoteException {
        this.name = name;
        this.groupName = groupName;
        members = new HashMap<String, Member>();
        members.put(name, this);
    }

    public Status sendMessage(Message m) {
        // Check whos alive
//            for member:members

                // try
                //nameList.add(groupName + "/" + member.getName())
                // catch

        // MessageOrdering.send(
        //

        return null;
    }

    public String getName() throws RemoteException {
        return name;
    }

    public String[] getMemberNames() {



        return members.keySet().toArray(new String[members.size()]);
    }

    public Class getCommunicationType() throws RemoteException {
        return null;
    }

    public Status joinGroup(Member m) throws RemoteException {
        if(!members.containsKey(m.getName())) {
            members.put(m.getName(),m);

            //Add members to m
            m.setMembers(members);


            //Send message to group
            return Status.CONNECTED_TO_GROUP;
        }
        return Status.NAME_EXISTS;
    }

    public void removeGroup() throws RemoteException {

    }

    public void setMembers(HashMap<String, Member> members) throws RemoteException {
        this.members = (HashMap<String, Member>) members.clone();
    }

    public Member getLeader() {
        return leader;
    }

    public void setLeader(Member leader) {
        this.leader = leader;
    }




    //AskForbeingLeader()
        //algorithm

    //registerGroup()
        //nameService = NameService()
        // nameService.reregisterGroup(
}
