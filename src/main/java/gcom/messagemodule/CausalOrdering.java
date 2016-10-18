package gcom.messagemodule;


import gcom.message.Message;

import java.rmi.RemoteException;
import java.util.*;

/**
 * Created by timmy on 03/10/16.
 */
public class CausalOrdering implements Ordering {
    private String name;

    private HashMap<String, Integer> vectorClock;
    private ArrayList<Message> delayQue;
    private int myTime = 0;

    public CausalOrdering(String name) {
        this.name = name;
        vectorClock = new HashMap<>();
        delayQue = new ArrayList<>();
        vectorClock.put(name, 0);
    }

    @Override
    public void setMessageStamp(Message m) {
        //if member not exist add to hashmap

        try {
            if (!vectorClock.containsKey(m.getFromName())) {
                vectorClock.put(m.getFromName(), 0);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        int time = vectorClock.get(name) + 1;
        vectorClock.put(name, time);

        try {
            m.setVectorClock((HashMap<String, Integer>) vectorClock.clone());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message[] orderMessage(Message m) {

        int timeJ = 0;
        try {
            if (!vectorClock.containsKey(m.getFromName())) {
                vectorClock.put(m.getFromName(), 0);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }


        HashSet<Message> passMessages = new HashSet<>();
        ArrayList<Message> myMessags = new ArrayList<>();
        try {
            HashMap<String, Integer> vectorI = vectorClock;
            delayQue.add(m);
            //loop all messages again
            for (int i = 0; i < delayQue.size(); i++) {
                for (Message msg : delayQue) {
                    HashMap<String, Integer> vectorJ = msg.getVectorClock();
                    timeJ = vectorJ.get(msg.getFromName());

                    if (m.getFromName().equals(name)) {
                        if (timeJ == myTime + 1) {
                            if (passMessages.add(msg)) {
                                myTime++;
                                myMessags.add(msg);
                            }
                        }

                    } else {
                        if ((timeJ == (vectorI.get(msg.getFromName()) + 1))
                                && isLessForAll(msg.getFromName(), vectorJ, vectorI)) {

                            if (passMessages.add(msg)) {
                                myMessags.add(msg);
                                updateClock(msg.getFromName());

                            }
                        }
                    }
                }
            }

            for (Message msg : myMessags) {
                delayQue.remove(msg);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }


        return myMessags.toArray(new Message[]{});
    }

    @Override
    public void setVectorClock(HashMap<String, Integer> vectorClock, String fromName) {
        String[] keys = vectorClock.keySet().toArray(new String[]{});
        for(String name:keys) {
            int time = vectorClock.get(name);
            if(!fromName.equals(name)) {
                for(int i = 0; i < time; i++) {
                    updateClock(name);
                }
            } else {
                for(int i = 1; i < time; i++) {
                    updateClock(name);
                }
            }

        }
    }


    private boolean isLessForAll(String fromName, HashMap<String, Integer> vectorJ,
                                 HashMap<String, Integer> vectorI) {

        String[] keys = vectorJ.keySet().toArray(new String[]{});
        for (String key : keys) {
            if (!key.equals(fromName)) {
                if (!(vectorJ.get(key) <= vectorI.get(key))) {
                    return false;
                }
            }
        }

        return true;
    }

    private void updateClock(String fromName) {
        if(!vectorClock.containsKey(fromName)) {
            vectorClock.put(fromName,0);
        }
        int time = vectorClock.get(fromName) + 1;
        vectorClock.put(fromName, time);
    }



    private void addToDelayQue(Message m) {

    }
}
