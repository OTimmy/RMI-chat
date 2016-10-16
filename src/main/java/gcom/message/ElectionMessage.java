package gcom.message;

import gcom.groupmodule.Member;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Containts the member to be the new leader for the group that
 * receives this message
 *
 * Created by c12ton on 10/14/16.
 */
public class ElectionMessage extends UnicastRemoteObject implements Message,Election{

    private Member leader;
    private String groupName;
    private String fromName;
    private String toName;

    /**
     * @param leader the new leader for the group
     */
    public ElectionMessage(Member leader) throws RemoteException {
        super();
        this.leader = leader;
    }

    @Override
    public Member getLeader() throws RemoteException{
        return leader;
    }

    @Override
    public MessageType getMessageType() throws RemoteException {
        return MessageType.ELECTION_MESSAGE;
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
}
