package gcom.message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by c12ton on 2016-10-06.
 */
public class ChatMessage extends UnicastRemoteObject implements Message,Chat{
    private String message;
    private String user;

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
    public void setGroupName(String groupName) {

    }

    @Override
    public String getGroupName() {
        return null;
    }
}