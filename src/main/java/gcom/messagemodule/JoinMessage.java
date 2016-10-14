package gcom.messagemodule;

import gcom.groupmodule.Member;

import java.rmi.RemoteException;

/**
 * Created by c12ton on 10/14/16.
 */
public class JoinMessage implements Join,Message {

    private Member member;
    public JoinMessage(Member member) {
        this.member = member;
    }

    public String getName() throws RemoteException {
        return member.getName();
    }

    @Override
    public Member getMember() throws RemoteException{
        return member;
    }

    @Override
    public MessageType getMessageType() throws RemoteException {
        return MessageType.JOIN_MESSAGE;
    }
}
