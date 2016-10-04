package gcom;

import gcom.communicationmodule.Communication;
import gcom.groupmodule.GroupMember;
import gcom.messagemodule.Message;
import gcom.messagemodule.MessageOrdering;
import gcom.nameservice.NameService;
import gcom.observer.Observer;
import gcom.observer.Subject;

/**
 * Created by c12ton on 9/29/16.
 */
public class GCOM implements Subject{


    private GroupMember member;
    private MessageOrdering messageOrdering;
    private Communication communication;


    private String nameSeriveAddress;
    private NameService nameService;
    public GCOM(String nameSerivceAddress) {
        //nameService = connectToNameService(nameServiceAddress)
        // if failed throw exception

    }

    public void sendMessageToGroup(String msg) {

        String[] membersName = member.getMemberNames();
        String group = member.getGroupName();
        Message message = messageOrdering.convertToMessage(membersName,msg);

        communication.sendMessage(group,membersName,message);
    }

    public void connectToGroup(String groupName,String name) {
        //GroupLeader leader = nameService.getGroupLeader(groupName)
        // Class b = leader.getCommunicationType()
        //member = new GroupMember(name,leader,comtype?)
        //leader.join(member)
    }

    public void createGroup() {
        //nameService.createGroup(String groupName, member)
    }

    public void leaveGroup() {
        //set member to null
    }

    public void registerObservers(Observer... obs) {

    }

    public void notifyObserver() {

        //notify of members leaving
        //notify of message received

    }
}
