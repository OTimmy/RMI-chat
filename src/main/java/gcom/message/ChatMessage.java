package gcom.message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * Created by c12ton on 2016-10-06.
 */
public class ChatMessage extends UnicastRemoteObject implements Message,Chat{
    private String message;
    private String user;
    private String groupName;

    private String fromName;
    private String toName;


    private HashMap<String,Integer> vectorClock;
    /**
     * @param user the username of the sender
     * @param message the chat message, null if non is needed
     * @throws RemoteException
     */
    public ChatMessage(String user, String message) throws RemoteException {
        super();
        this.message = message;
        this.user = user;
    }

    @Override
    public String getMessage() throws RemoteException {
        return message;
    }

    @Override
    public String getUser() throws RemoteException {
        return user;
    }

    @Override
    public MessageType getMessageType() throws RemoteException {
        return MessageType.CHAT_MESSAGE;
    }

    @Override
    public void setGroupName(String groupName) throws RemoteException{
        this.groupName = groupName;
    }

    @Override
    public String getGroupName() throws RemoteException{
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
