package gcom.messagemodule;

import gcom.groupmodule.Member;

import java.rmi.RemoteException;

/**
 * Created by timmy on 03/10/16.
 */
public interface MessageOrdering {

    /**
     * Will update current vector clock and include it in the message
     * @param message the message that should be sent.
     */
    void setMessageStamp(Message message);  //To be replaced with something alone the lines, setMessageStamp


    /**
     * Order incoming messages, and places inconsisten ones in a pending que
     * @param m
     */
    void orderMessage(Message m);

}
