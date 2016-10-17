package gcom.message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 2016-09-30.
 * Defines what type of message is being sent
 */
public interface Message extends Remote{
    MessageType getMessageType() throws RemoteException;
    void setGroupName(String groupName) throws RemoteException;
    String getGroupName() throws RemoteException;
    void setFromName(String fromName) throws RemoteException;
    void setToName(String toName) throws RemoteException;
    String getFromName() throws RemoteException;
    String getToName() throws RemoteException;

    void setVectorClock();
}
