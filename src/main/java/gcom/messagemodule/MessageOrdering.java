package gcom.messagemodule;

import gcom.groupmodule.Member;
import gcom.status.Status;

import java.rmi.RemoteException;

/**
 * Created by timmy on 03/10/16.
 */
public interface MessageOrdering {

    /**
     * @param
     * @param msg
     * @return
     */
    Message convertToMessage(Member[] members, String msg) throws RemoteException;


    /**
     *
     */
    void convertFromMessage(Message m); //updateMessageData

    //orderingMessage


    //void orderIncomingMessage(Message m)
}
