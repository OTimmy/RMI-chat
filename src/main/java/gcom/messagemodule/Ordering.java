package gcom.messagemodule;

import gcom.message.Message;

import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Created by timmy on 03/10/16.
 */
public interface Ordering {

    /**
     * Will update current vector clock and include it in the message
     * @param message the message that should be sent.
     */
    void setMessageStamp(Message message, String[] names);  //To be replaced with something alone the lines, setMessageStamp


    /**
     * Order incoming messages, and places inconsisten ones in a pending que
     * @param m
     */
    Message[] orderMessage(Message m);


    void setVectorClock(HashMap<String, Integer> vectorClock, String fromName);

    void removeVector(String name) throws RemoteException;

}
