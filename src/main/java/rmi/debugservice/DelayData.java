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

    private ArrayList<Message> delay;

    public DelayData(String groupName, String name, ArrayList<Message> delay) throws RemoteException {
        super();
        this.groupName = groupName;
        this.name = name;
        this.delay = delay;

    }

    @Override
    public String getName() throws RemoteException{
        return name;
    }

    @Override
    public String getGroupName() throws RemoteException{
        return groupName;
    }

    @Override
    public ArrayList<Message> getDelayQue() throws RemoteException{
        return delay;
    }
}
