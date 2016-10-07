package gcom.messagemodule;

import gcom.status.Status;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by c12ton on 2016-10-06.
 */
public class ClientMessage extends UnicastRemoteObject implements Message{
    private String message;
    private Status status;
    private String user;
    /**
     * @param user the username of the sender
     * @param message the chat message, null if non is needed
     * @param status the status of the  message, null if non is needed.
     * @throws RemoteException
     */
    public ClientMessage(String user,String message,Status status) throws RemoteException {
        super();
        this.message = message;
        this.status = status;
        this.user = user;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public Status getStatusMessage() throws RemoteException {
        return status;
    }

    @Override
    public String getChatMessage() throws RemoteException {
        return message;
    }
}
