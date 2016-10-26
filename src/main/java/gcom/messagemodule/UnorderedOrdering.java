package gcom.messagemodule;

import gcom.message.Message;

import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Created by c12ton on 2016-10-04.
 */
public class UnorderedOrdering implements Ordering {

    private String name;
    private Message message;
    private HashMap<String,Integer> vectorClock;
    public UnorderedOrdering(String name) {
        this.name = name;
        vectorClock = new HashMap<>();
        vectorClock.put(name,0);
    }

    @Override
    public void setMessageStamp(Message message, String[] names){
        for(String name:names) {
            if(!vectorClock.containsKey(name)) {
            }
        }

        try {
            message.setVectorClock((HashMap<String, Integer>) vectorClock.clone());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
     }

    @Override
    public Message[] orderMessage(Message m) {
        return new Message[]{m};
    }

    @Override
    public void setVectorClock(HashMap<String, Integer> vectorClock, String fromName) {}

    @Override
    public void removeVector(String name) throws RemoteException {}


}
