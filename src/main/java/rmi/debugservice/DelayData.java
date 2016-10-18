package rmi.debugservice;

import gcom.message.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by c12ton on 10/18/16.
 */
public class DelayData extends UnicastRemoteObject implements DelayContainer {
    private String groupName;

    private String name;
    private String fromName;
    private String toName;
    private String message;

    public DelayData(String groupName, String fromName, String toName) throws RemoteException {
        super();
        this.groupName = groupName;
        this.fromName = fromName;
        this.toName = toName;
    }

    public String getGroupName() throws RemoteException{
        return groupName;
    }

    @Override
    public String getFromName() {
        return fromName;
    }

    @Override
    public String getToName() {
        return toName;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String msg) throws RemoteException {
        this.message = msg;
    }
}
