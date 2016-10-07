package gcom.messagemodule;

import gcom.status.Status;

import java.rmi.RemoteException;

/**
 * Created by timmy on 03/10/16.
 */
public interface MessageOrdering {

    /**
     *
     * @param membersName
     * @param msg
     * @return
     */
    Message convertToMessage(String user, String[] membersName, String msg, Status status) throws RemoteException;

    /**
     *
     */
    void convertFromMessage(Message m); //updateMessageData

    //orderingMessage


    //void orderIncomingMessage(Message m)
}
