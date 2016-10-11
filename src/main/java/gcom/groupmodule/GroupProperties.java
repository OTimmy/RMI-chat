package gcom.groupmodule;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by c12ton on 10/10/16.
 */
public class GroupProperties extends UnicastRemoteObject implements Properties{

    private String groupName;

    private Class comtype;
    private Class messagetype;

    public GroupProperties(Class comtype,Class messagetype, String groupName) throws RemoteException {
        this.comtype     = comtype;
        this.messagetype = messagetype;
        this.groupName = groupName;
    }

    public String getGroupName() throws RemoteException{
        return groupName;
    }

    public Class getMessagetype() throws RemoteException {
        return messagetype;
    }

    public Class getComtype() throws RemoteException {
        return comtype;
    }

}
