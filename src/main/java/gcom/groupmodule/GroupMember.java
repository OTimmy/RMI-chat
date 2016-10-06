package gcom.groupmodule;

import gcom.communicationmodule.NonReliableCommunication;
import gcom.messagemodule.Message;
import gcom.observer.Observer;
import gcom.observer.Subject;
import gcom.status.GCOMException;
import gcom.status.Status;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * Created by c12ton on 9/29/16.
 */
public class GroupMember extends UnicastRemoteObject implements Member, Subject {
    private String name;
    private String groupName;
    private HashMap<String,Member> members;
    private Member leader;

    private final String communicationType;

    public GroupMember(String name, String groupName, String communicationType) throws RemoteException {
        this.name = name;
        this.groupName = groupName;
        this.communicationType = communicationType;

        members = new HashMap<String, Member>();
        members.put(name, this);
    }

    public String getName() throws RemoteException {
        return name;
    }

    public String[] getMemberNames() {
        //if member is dead
            //notifyObserver

        return members.keySet().toArray(new String[members.size()]);
    }

    public String getCommunicationType() throws RemoteException {
        return communicationType;
    }

    /**
     *  Adding groups current members to new coming.
     * @param m member to joing group
     * @throws RemoteException
     * @throws GCOMException
     */
    public void joinGroup(Member m) throws RemoteException, GCOMException {

        if(members.containsKey(m.getName())) {
            throw new GCOMException(Status.NAME_EXISTS);
        }

        if(!members.containsKey(m.getName())) {
            members.put(m.getName(),m);
            //Add current know members to m
            m.setMembers(members);
            //notifyMembers that a join has occured!
        }
    }

    public void removeGroup() throws RemoteException {
        //Contact name service
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

    public void registerObservers(Observer... obs) {

    }

    public void notifyObserver() {

    }

    public String getGroupName() {
        return groupName;
    }


    //AskForbeingLeader()
        //algorithm

    //registerGroup()
        //nameService = NameService()
        // nameService.reregisterGroup(
}
