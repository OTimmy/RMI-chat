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


        //add m in hold-que
        //messageHold.add(m.getFrom(),message)
        //vectorI = vectorClock
        //loop  que
            //vectorJ = m.getVector();
            //if(vectorJ[m.getFrom] == (vectorI[m.getFrom]+1) && isLessForAll(m.getFrom)
                // remove from linkedQue and add to pass que


        try {
            if(!vectorClock.containsKey(m.getFromName())) {
                vectorClock.put(m.getFromName(),0);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        ArrayList<Message> passMessages = new ArrayList<>();

        Set keys = vectorClock.keySet();
        if(checkVectorClocks(m, keys)){
            passMessages.add(m);
            try {
                if(!m.getFromName().equals(name)) {
                    int v = vectorClock.get(m.getFromName());
                    vectorClock.put(m.getFromName(), v + 1);
                }
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
                    if(!msg.getFromName().equals(name)) {
                        int v = vectorClock.get(msg.getFromName());
                        vectorClock.put(msg.getFromName(), v + 1);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                loops = 0;
            }else{
                loops++;
            }
        }
        return passMessages.toArray(new Message[]{});
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

            int compTime = time;

            if(!from.equals(name)){
                compTime += 1;
            }

            if(mem.equals(from) && compTime != msgTime){
                return false;

            }else if(time < msgTime){
                return false;
            }
        }
        return true;
    }


//    private void updateVectorClock()
}
