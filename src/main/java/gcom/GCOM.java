package gcom;

import gcom.groupmodule.GroupMember;
import gcom.messagemodule.Message;
import gcom.observer.Observer;
import gcom.observer.Subject;

/**
 * Created by c12ton on 9/29/16.
 */
public class GCOM implements Subject{


    private GroupMember member;
    public GCOM() {

        //nameService = connectToNameService()
        // if failed throw exception

    }

    public void sendMessageToGroup(Message m) {
        //member.send(m)
    }

    public void connectToGroup(String groupName,String name) {
        //GroupLeader leader = nameService.getGroupLeader(groupName)
        //member = new GroupMember(name,leader)
        //leader.join(member)
    }

    public void createGroup() {
        //nameService.createGroup(String groupName, member)
    }

    public void leaveGroup() {
        //remove member from nameService
        //set member to null
    }

    public void registerObservers(Observer... obs) {

    }

    public void notifyObserver() {

    }
}
