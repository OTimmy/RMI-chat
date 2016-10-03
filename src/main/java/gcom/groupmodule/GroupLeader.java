package gcom.groupmodule;

/**
 * Created by timmy on 03/10/16.
 */
public class GroupLeader implements Leader {
    private Member member;

    public GroupLeader(Member member) {
        this.member = member;
    }

    public void joinGroup(Member m) {
        //list members = member.getMemberList().clone
        //m.setMembers(members)
        // member.sendMessage(Message.type.JOIN,new messageJOIN(Member))
    }

}
