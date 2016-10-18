package gcom.message;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * Created by c12ton on 10/13/16.
 * Is used to send leave messages to members
 */
public class LeaveMessage implements Message,Leave,Serializable, Cloneable{
    private String name;
    private String groupName;
    private String fromName;
    private String toName;


    private HashMap<String,Integer> vectorClock;
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
    public void setVectorClock(HashMap<String,Integer> vectorClock) throws RemoteException{
        this.vectorClock = vectorClock;
    }

    @Override
    public HashMap<String, Integer> getVectorClock() throws RemoteException{
        return vectorClock;
    }
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
