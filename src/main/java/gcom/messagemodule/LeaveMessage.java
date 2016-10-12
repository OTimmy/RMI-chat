package gcom.messagemodule;

import gcom.status.Status;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by c12ton on 10/12/16.
 */
public class LeaveMessage extends UnicastRemoteObject implements Leave{

    private String name;
    public LeaveMessage(String name) throws RemoteException {
        super();
        this.name = name;
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

}
