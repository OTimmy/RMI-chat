package gcom.messagemodule;


import gcom.status.Status;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

/**
 * Created by timmy on 03/10/16.
 */
public class CausalMessageOrdering implements MessageOrdering{
    private HashMap<String,Integer> vectorClock;
    private BlockingQueue inQue;

    public CausalMessageOrdering() {
        vectorClock = new HashMap<>();
    }


    @Override
    public Message convertToMessage(String user, String[] membersName, String msg, Status status) throws RemoteException {
        return null;
    }

    @Override
    public void convertFromMessage(Message m) {

    }

}
