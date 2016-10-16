package gcom.message;

import gcom.groupmodule.Member;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by c12ton on 10/14/16.
 */
public class JoinMessage extends UnicastRemoteObject implements Join,Message {

    private Member member;
    private String groupName;
    public JoinMessage(Member member) throws RemoteException {
        super();
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

    @Override
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String getGroupName() {
        return groupName;
    }
}
