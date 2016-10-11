package gcom.messagemodule;


import gcom.groupmodule.Member;
import gcom.status.Status;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

/**
 * Created by timmy on 03/10/16.
 */
public class CausalMessageOrdering implements MessageOrdering{
    private String name;

    private HashMap<String,Integer> vectorClock;
    private BlockingQueue inQue;
    // have a delay que, that's used of messages that arent complete, and check this every time theres a new message in inQue

    public CausalMessageOrdering(String name) {
        this.name = name;
        vectorClock = new HashMap<>();
    }


    @Override
    public Message convertToMessage(Member[] members, String msg) throws RemoteException {
        return new ChatMessage(name,msg);
    }

    @Override
    public void convertFromMessage(Message m) {

    }

}
