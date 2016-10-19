package gcom.groupmodule;

import gcom.message.*;
import rmi.RMIServer;
import rmi.nameservice.NameService;
import gcom.observer.Observer;
import gcom.observer.ObserverEvent;
import gcom.observer.Subject;
import gcom.status.GCOMError;
import gcom.status.GCOMException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by c12ton on 10/10/16.
 */
public class GroupManager implements Manager,Subject{

    private ArrayList<Observer> observers;
    private Properties properties;
    private LinkedHashMap<String,Member> members;
    private Member member;
    private Member leader;
    private NameService nameService;

    public GroupManager(String host) throws RemoteException, NotBoundException {
        nameService = RMIServer.getNameService(host);
        members = new LinkedHashMap<>();
        observers = new ArrayList<>();
    }

    @Override
    public String[] getGroups() throws RemoteException {
        return nameService.getGroups().keySet().toArray(new String[]{});
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    @Override
    public Properties getGroupProperties(String groupName) throws RemoteException {
        return nameService.getGroupLeader(groupName).getProperties();
    }

    /**
     * @param properties defines the name,communication type and message ordering
     *                   for the group
     * @param name the name of the user
     * @throws RemoteException
     */
    @Override
    public void createGroup(Properties properties,String name) throws RemoteException {
        this.properties = properties;
        member = new GroupMember(name,this);

        leader = member;
        nameService.registerGroup(properties.getGroupName(),member);
        members.put(name,member);
    }


    @Override
    public Member[] joinGroup(Properties properties, String name) throws GCOMException, RemoteException {
        this.properties = properties;
        member = new GroupMember(name,this);
        leader = nameService.getGroupLeader(properties.getGroupName());
        leader.requestToJoin(member);
        return getMembers();
    }


    @Override
    public void addMember(Member m) throws RemoteException {
        members.put(m.getName(),m);
    }

    @Override
    public void removeMember(String name) throws RemoteException {
        members.remove(name);
    }

    @Override
    public Member[] getMembers() throws RemoteException {


        //Checking if leader is still alive
        try {
            leader.getName();
        }catch (Exception e) {
            boolean isLeader = electLeader(properties.getGroupName(),member);
            if(isLeader) {
                Message m = new ElectionMessage(member);
                leader = member;
                notifyObserver(ObserverEvent.MESSAGE_TO_GROUP,m);
            } else {
                //In case of missing the message of a new leader
                setLeader(nameService.getGroupLeader(properties.getGroupName()));
            }
        }

        String[] names = members.keySet().toArray(new String[]{});
        ArrayList<Member> membs = new ArrayList<>();
        for(String name:names) {
            try {
                Member m = members.get(name);
                //Check if member is alive
                m.getName();
                membs.add(m);
            } catch (Exception e) {
                removeMember(name);
                Message m = new LeaveMessage(name);
                notifyObserver(ObserverEvent.MESSAGE_TO_GROUP,m);
            }
        }

        return membs.toArray(new Member[]{});
    }

    @Override
    public boolean electLeader(String groupName, Member m) throws RemoteException {
        return nameService.replaceLeader(groupName,m);
    }

    @Override
    public void leaderMemberJoin(Member m) throws RemoteException, GCOMException {
        if(members.containsKey(m.getName())) {
            throw new GCOMException(GCOMError.NAME_EXISTS);
        }
        //add members to newly joined member
        members.put(m.getName(),m);
        Member[] currentMembers = getMembers();

        m.setMember(currentMembers);

        Message message = new JoinMessage(m);
        notifyObserver(ObserverEvent.MESSAGE_TO_GROUP,message);
    }

    @Override
    public void removeGroup() {
        try {
            nameService.removeGroup(getProperties().getGroupName());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void leaderDeleteGroup() {
        try {
            Message message = new DeleteMessage();
            notifyObserver(ObserverEvent.MESSAGE_TO_GROUP,message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setLeader(Member leader) {
        this.leader = leader;
    }

    @Override
    public String getName() {
        try {
            return member.getName();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void receivedMessage(Message m) {
        try {
            notifyObserver(ObserverEvent.RECEIVED_MESSAGE,m);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void registerObservers(Observer... obs) throws RemoteException {
        for(Observer ob:obs) {
            observers.add(ob);
            //member.registerObserver(ob)
        }
        //Add observer for memberleave
    }

    @Override
    public  void notifyObserver(ObserverEvent e, Message m) throws RemoteException {
        for(Observer ob:observers) {
            try {
                ob.update(e,m);
            } catch (GCOMException e1) {
                e1.printStackTrace();
            }
        }
    }
}
