package gcom.message;

import gcom.groupmodule.Member;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * Created by c12ton on 10/14/16.
 */
public class JoinMessage extends UnicastRemoteObject implements Join,Message, Cloneable {

    private Member member;
    private String groupName;
    private String fromName;
    private String toName;

    private HashMap<String,Integer> vectorClock;

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

    @Override
    public void setFromName(String fromName) throws RemoteException {
        this.fromName = fromName;
    }

    @Override
    public void setToName(String toName) throws RemoteException {
        this.toName = toName;
    }

    @Override
    public String getFromName() throws RemoteException {
        return fromName;
    }

    @Override
    public String getToName() throws RemoteException {
        return toName;
    }

    @Override
    public void setVectorClock(HashMap<String, Integer> vectorClock) throws RemoteException{
        this.vectorClock = vectorClock;
    }

    @Override
    public HashMap<String, Integer> getVectorClock() throws RemoteException{
        return vectorClock;
    }

}
