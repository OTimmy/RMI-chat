package gcom.groupmodule;

import gcom.communicationmodule.Communication;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;

/**
 * Created by c12ton on 9/29/16.
 */
public class Member extends UnicastRemoteObject implements InterfaceMember {
    private String name;
    private String groupName;
    private HashSet<Communication> membsCom;
    private Communication leader = null;

    protected Member(String name, String groupName) throws RemoteException {
        this.name = name;
        this.groupName = groupName;
    }

    public void setLeaderCom(Communication leader) {

    }

    public void setMembersCom(Communication[] membsCom) {
//        this.membsCom = membsCom;
    }

    public String getGroupName() throws RemoteException {
        return null;
    }

    public void addNewMember(InterfaceMember member) throws RemoteException {

    }

    public String getName() throws RemoteException {
        return name;
    }

}
