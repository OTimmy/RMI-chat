package gcom.messagemodule;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by c12ton on 2016-10-06.
 */
public class ChatMessage extends UnicastRemoteObject implements Message{
    private String message;
    public ChatMessage(String message) throws RemoteException {
        super();
        this.message = message;
    }

    @Override
    public String getMessage() throws  RemoteException{
        return message;
    }
}
