package gcom.messagemodule;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by c12ton on 2016-10-06.
 */
public class MemberMessage extends UnicastRemoteObject implements Message{
    private String message;
    private String user;
    private MessageType type;

    /**
     * @param user the username of the sender
     * @param message the chat message, null if non is needed
     * @param type specifies the type of message.
     * @throws RemoteException
     */
    public MemberMessage(String user, String message, MessageType type) throws RemoteException {
        super();
        this.message = message;
        this.user = user;
        this.type = type;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public MessageType getMessageType() throws RemoteException {
        return type;
    }

    @Override
    public String getChatMessage() throws RemoteException {
        return message;
    }
}
