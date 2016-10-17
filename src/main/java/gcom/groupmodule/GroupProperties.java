package gcom.groupmodule;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by c12ton on 10/10/16.
 */
public class GroupProperties extends UnicastRemoteObject implements Properties{

    private String groupName;

    private String comtype;
    private String messagetype;

    public GroupProperties(String comtype,String messagetype, String groupName) throws RemoteException {
        this.comtype     = comtype;
        this.messagetype = messagetype;
        this.groupName = groupName;
    }

    public String getGroupName() throws RemoteException{
        return groupName;
    }

    public String getMessagetype() throws RemoteException {
        return messagetype;
    }

    public String getComtype() throws RemoteException {
        return comtype;
    }


}
