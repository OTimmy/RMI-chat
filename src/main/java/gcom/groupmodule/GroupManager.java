package gcom.groupmodule;

import gcom.messagemodule.MemberMessage;
import gcom.messagemodule.Message;
import gcom.messagemodule.MessageType;
import gcom.nameservice.NameService;
import gcom.nameservice.NameServiceConcrete;
import gcom.observer.Observer;
import gcom.observer.ObserverEvent;
import gcom.observer.Subject;
import gcom.status.GCOMException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by c12ton on 10/10/16.
 */
public class GroupManager implements Manager,Subject{


    private Observer observerMemberLeave; //If member is detected here to have died
    private Observer observerMessages;

    private ArrayList<Observer> observers;
    private Properties properties;
    private LinkedHashMap<String,Member> members;
    private Member member;
    private NameService nameService;

    public GroupManager(String host) throws RemoteException, NotBoundException {
        nameService = NameServiceConcrete.getNameService(host);
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
        nameService.registerGroup(properties.getGroupName(),member);
        members.put(properties.getGroupName(),member);
    }


    @Override
    public Member[] joinGroup(String groupName, String name) throws GCOMException, RemoteException {
        member = new GroupMember(name,this);
        Member leader = nameService.getGroupLeader(groupName);
        leader.requestToJoin(member);
        return members.values().toArray(new Member[]{});
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

        String[] names = members.keySet().toArray(new String[]{});
        ArrayList<Member> membs = new ArrayList<>();

        for(String name:names) {
            try {
                Member m = members.get(name);
                m.getName();
                membs.add(m);
            } catch (RemoteException e) {
                Message m = new MemberMessage(name,null, MessageType.LEAVE_MESSAGE);
                notifyObserver(ObserverEvent.MEMBER_HAS_LEFT,m);
            }
        }

        return membs.toArray(new Member[]{});
    }

    //Make receivedMessage to Generic, then convert it depending on what type
    @Override
    public void receivedMessage(Message m) {
        try {
            notifyObserver(ObserverEvent.CHAT_MESSAGE,m);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean memberExist(Member m) throws RemoteException, GCOMException {
        return false;
    }

    @Override
    public void registerObservers(Observer... obs) throws RemoteException {
        for(Observer ob:obs) {
            observers.add(ob);
        }
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
