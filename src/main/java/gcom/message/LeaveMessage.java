package gcom.message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by c12ton on 10/13/16.
 * Is used to send leave messages to members
 */
public class LeaveMessage extends UnicastRemoteObject implements Message,Leave{
    private String name;
    private String groupName;

    /**
     * @param name the name of the user that left the group
     * @throws RemoteException
     */
    public LeaveMessage(String name) throws RemoteException {
        this.name = name;
    }

    @Override
    public String getName() throws RemoteException{
        return name;
    }

    @Override
    public MessageType getMessageType() throws RemoteException {
        return MessageType.LEAVE_MESSAGE;
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
