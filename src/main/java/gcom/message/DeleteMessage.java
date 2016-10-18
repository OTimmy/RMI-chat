package gcom.message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * Created by c12ton on 10/18/16.
 */
public class DeleteMessage extends UnicastRemoteObject implements Delete,Message, Cloneable{
    protected DeleteMessage() throws RemoteException {}

    private String groupName;
    private String fromName;
    private String toName;
    private HashMap<String,Integer> vectorClock;

    @Override
    public MessageType getMessageType() throws RemoteException {
        return MessageType.DELETE_MESSAGE;
    }

    @Override
    public void setGroupName(String groupName) throws RemoteException {
        this.groupName = groupName;
    }

    @Override
    public String getGroupName() throws RemoteException {
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
    public void setVectorClock(HashMap<String, Integer> vectorClock) throws RemoteException {
        this.vectorClock = vectorClock;
    }

    @Override
    public HashMap<String, Integer> getVectorClock() throws RemoteException {
        return null;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
