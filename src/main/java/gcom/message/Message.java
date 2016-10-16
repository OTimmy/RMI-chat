package gcom.message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 2016-09-30.
 * Defines what type of message is being sent
 */
public interface Message extends Remote{
    MessageType getMessageType() throws RemoteException;
    void setGroupName(String groupName);
    String getGroupName();
}
