package gcom.messagemodule;

import gcom.status.Status;

import java.rmi.RemoteException;

/**
 * Created by timmy on 09/10/16.
 */
public class JoinMessage implements Message{
    private String user;
    public JoinMessage(String user) {
        this.user = user;
    }

    @Override
    public String getUser() throws RemoteException {
        return user;
    }

    @Override
    public Status getStatusMessage() throws RemoteException {
        return null;
    }

    @Override
    public String getChatMessage() throws RemoteException {
        return null;
    }
}
