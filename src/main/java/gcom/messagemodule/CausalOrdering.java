package gcom.messagemodule;


import gcom.message.Message;

import java.rmi.RemoteException;
import java.util.*;

/**
 * Created by timmy on 03/10/16.
 */
public class CausalOrdering implements Ordering {
    private String name;

    private HashMap<String,Integer> vectorClock;
    private ArrayList<Message> delayQue;
    // have a delay que, that's used of messages that arent complete, and check this every time theres a new message in inQue

    public CausalOrdering(String name) {
        this.name = name;
        vectorClock = new HashMap<>();
        delayQue = new ArrayList<>();
    }

    @Override
    public void setMessageStamp(Message message) {
        //if member not exist add to hashmap

        try {
            if(!vectorClock.containsKey(message.getFromName())) {
                vectorClock.put(message.getFromName(),0);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        int time = vectorClock.get(name);
        vectorClock.put(name, time+1);

        try {
            message.setVectorClock((HashMap<String, Integer>) vectorClock.clone());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message[] orderMessage(Message m) {

        ArrayList<Message> passMessages = new ArrayList<>();

        Set keys = vectorClock.keySet();
        if(checkVectorClocks(m, keys)){
            passMessages.add(m);
            try {
                int v = vectorClock.get(m.getFromName());
                vectorClock.put(m.getFromName(), v+1);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else {
            delayQue.add(m);
            return null;
        }

        int loops = 0;

        while(loops < delayQue.size()){

            Message msg = delayQue.get(loops);

            if(checkVectorClocks(msg, keys)){
                passMessages.add(delayQue.remove(loops));
                try {
                    int v = vectorClock.get(msg.getFromName());
                    vectorClock.put(msg.getFromName(), v+1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                loops = 0;
            }else{
                loops++;
            }
        }
        return (Message[]) passMessages.toArray();
    }

    private boolean checkVectorClocks(Message m, Set keys){
        int time;
        int msgTime;
        String from = null;
        try {
            from = m.getFromName();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        for(Object mem : keys){
            time = vectorClock.get(mem);

            try {
                msgTime = m.getVectorClock().get(mem);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }

            if(mem.equals(from) /*&& time +1 != newTime*/){
                return false;

            }else if(time < msgTime){
                return false;
            }
        }
        return true;
    }
}
