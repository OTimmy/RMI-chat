package gcom.messagemodule;


import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

/**
 * Created by timmy on 03/10/16.
 */
public class CausalMessageOrdering implements MessageOrdering{
    private HashMap<String,Integer> vectorClock;
    private BlockingQueue inMessageQue;

    public CausalMessageOrdering() {
        vectorClock = new HashMap<>();
    }

    @Override
    public Message convertToMessage(String[] membersName, String msg) {

        return null;
    }

    @Override
    public void convertFromMessage(Message m) {

    }

}
