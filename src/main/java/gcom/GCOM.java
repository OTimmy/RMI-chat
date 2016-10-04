package gcom;

import gcom.communicationmodule.Communication;
import gcom.communicationmodule.CommunicationFactory;
import gcom.communicationmodule.NonReliableCommunication;
import gcom.groupmodule.GroupMember;
import gcom.groupmodule.Member;
import gcom.messagemodule.CausalMessageOrdering;
import gcom.messagemodule.Message;
import gcom.messagemodule.MessageOrdering;
import gcom.nameservice.NameService;
import gcom.nameservice.NameServiceInterFace;
import gcom.observer.Observer;
import gcom.observer.Subject;
import gcom.status.GCOMException;
import gcom.status.Status;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Created by c12ton on 9/29/16.
 */
public class GCOM implements Subject{


    private GroupMember member;
    private MessageOrdering messageOrdering;
    private Communication communication;
    private NameServiceInterFace nameService;


    public GCOM(String host) throws RemoteException,NotBoundException {
        nameService = NameService.getNameService(host);
    }

    public Status sendMessageToGroup(String msg) {

        String[] membersName = member.getMemberNames();
        String group = member.getGroupName();
        Message message = messageOrdering.convertToMessage(membersName,msg);

        communication.sendMessage(group,membersName,message);

        return null;
    }

    /**
     * Try to join group trough leader of a group by given group name
     * @param groupName the group to join
     * @param username the users username for that group
     * @return a list of member names
     * @throws RemoteException
     * @throws GCOMException in that member already exists
     */
    public String[] connectToGroup(String groupName,String username) throws RemoteException, GCOMException {

         HashMap<String,Member> leaders = nameService.getGroups();
         Member leader = leaders.get(groupName);

        communication = createCommunication(leader.getCommunicationType());
        messageOrdering = new CausalMessageOrdering();

        member = new GroupMember(username,groupName,leader.getCommunicationType());
        leader.joinGroup(member);

        return member.getMemberNames();
    }

    public void createGroup(String groupName, String username,String comType) {
        //nameService.createGroup(String groupName, member)
    }

    public void leaveGroup() {
        //set member to null
    }


    //createListener for receive
    //createListener for sending

    /**
     * Creates a communcation with repsect to given type.
     * And add appropiate listeners and observers
     * @param type of
     * @return
     */
    private Communication createCommunication(String type) {
        if(type.equals(NonReliableCommunication.class.getName())) {
            return CommunicationFactory.createNonReliableCommunication();
        }

        return null;
    }

    public void registerObservers(Observer... obs) {

    }

    public void notifyObserver() {
        //notify of members leaving
        //notify of message received
    }
}
