package gcom.groupmodule;

import gcom.messagemodule.Message;
import gcom.status.GCOMException;
import gcom.status.GCOMError;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by c12ton on 9/29/16.
 */
public class GroupMember extends UnicastRemoteObject implements Member{


    private Manager manager;
    private String name;

    public GroupMember(String name, Manager manager) throws RemoteException {
        this.name = name;
        this.manager = manager;
    }

    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public Properties getProperties() throws RemoteException {
        return manager.getProperties();
    }


    @Override
    public void requestToJoin(Member m) throws RemoteException, GCOMException {
        manager.leaderMemberJoin(m);
    }

    @Override
    public void sendMessage(Message m) throws RemoteException {
        manager.receivedMessage(m);
    }

    @Override
    public void setMember(Member ...member) throws RemoteException {
       for(Member m:member) {
           manager.addMember(m);
       }
    }

    public void setLeader(Member leader) {}

}
