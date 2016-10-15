package gcom.messagemodule;


import gcom.message.Message;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

/**
 * Created by timmy on 03/10/16.
 */
public class CausalOrdering implements Ordering {
    private String name;

    private HashMap<String,Integer> vectorClock;
    private BlockingQueue inQue;
    // have a delay que, that's used of messages that arent complete, and check this every time theres a new message in inQue

    public CausalOrdering(String name) {
        this.name = name;
        vectorClock = new HashMap<>();
    }

    @Override
    public void setMessageStamp(Message message) {

    }

    @Override
    public void orderMessage(Message m) {

    }
}
