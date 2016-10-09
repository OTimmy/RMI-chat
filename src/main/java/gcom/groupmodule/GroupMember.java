package gcom.groupmodule;

import gcom.messagemodule.ClientMessage;
import gcom.messagemodule.JoinMessage;
import gcom.messagemodule.Message;
import gcom.nameservice.NameServiceConcrete;
import gcom.observer.Observer;
import gcom.observer.ObserverEvent;
import gcom.observer.Subject;
import gcom.nameservice.NameService;
import gcom.status.GCOMException;
import gcom.status.Status;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by c12ton on 9/29/16.
 */
public class GroupMember extends UnicastRemoteObject implements Member, Subject {

    private ArrayList<Observer> observers;
    private String name;
    private String groupName;
    private String host;
    private HashMap<String,Member> members;
    private Member leader;

    private final String communicationType;

    public GroupMember(String host,String name, String groupName, String communicationType) throws RemoteException {
        this.name = name;
        this.groupName = groupName;
        this.communicationType = communicationType;
        this.host = host;

        observers = new ArrayList<>();
        members = new HashMap<>();
        members.put(name, this);
    }

    public String getName() throws RemoteException {
        return name;
    }

    public String getCommunicationType() throws RemoteException {
        return communicationType;
    }

    @Override
    public void sendMessageToMember(Message m) {
        notifyObserver(ObserverEvent.MESSAGE, m);
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
            Member[] membs = members.values().toArray(new Member[]{});
            m.setMembers(membs);
            Message mJoin = new JoinMessage(m.getName());
            notifyObserver(ObserverEvent.MEMBER_JOINED,mJoin);
        }
    }

    public void removeGroup() {
        //Contact name service
    }

    @Override
    public void createGroup(String groupName) {
        NameServiceConcrete.registerGroup(host,groupName,this);
    }

    @Override
    public void setMembers(Member[] members) throws RemoteException {
        for(Member m:members) {
            this.members.put(m.getName(),m);
        }
    }

    public Member getLeader() {
        return leader;
    }

    public void setLeader(Member leader) {
        this.leader = leader;
    }

    @Override
    public void registerObservers(Observer... obs) throws RemoteException {
        for(Observer ob:obs) {
            observers.add(ob);
        }
    }

    public void notifyObserver(ObserverEvent e, Message m) {
        for(Observer ob:observers) {
            ob.update(e,m);
        }
    }

    public String getGroupName() {
        return groupName;
    }
}
