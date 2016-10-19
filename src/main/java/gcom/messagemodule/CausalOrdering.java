package gcom.messagemodule;


import gcom.message.Message;

import java.rmi.RemoteException;
import java.util.*;

/**
 * Created by timmy on 03/10/16.
 */
public class CausalOrdering implements Ordering {
    private String name;

    protected HashMap<String, Integer> vectorClock;
    protected ArrayList<Message> holdQue;
    private int myTime = 0;

    public CausalOrdering(String name) {
        this.name = name;
        vectorClock = new HashMap<>();
        holdQue = new ArrayList<>();
        vectorClock.put(name, 0);
    }

    @Override
    public void setMessageStamp(Message m, String[] names) {
        for(String name:names) {
            if(!vectorClock.containsKey(name)) {
//                vectorClock.put(name,0);
                setClockForName(name,0);
            }
        }

//        int time = vectorClock.get(name) + 1;
//        vectorClock.put(name, time);
        updateClock(name);

        try {
            m.setVectorClock((HashMap<String, Integer>) vectorClock.clone());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message[] orderMessage(Message m) {

        try {
            if (!vectorClock.containsKey(m.getFromName())) {
                vectorClock.put(m.getFromName(), 0);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        LinkedHashSet<Message> passMessages = new LinkedHashSet<>();

        try {
            HashMap<String, Integer> vectorI = vectorClock;
            addToDelayQue(m);

            //loop all messages again
            for (int i = 0; i < holdQue.size(); i++) {
                for (Message msg : holdQue) {
                    HashMap<String, Integer> vectorJ = msg.getVectorClock();
                    int timeJ = vectorJ.get(msg.getFromName());

                    if (msg.getFromName().equals(name)) {
                        if (timeJ == myTime + 1) {
                            if (passMessages.add(msg)) {
                                myTime++;
                            }
                        }

                    } else {
                        if ((timeJ == (vectorI.get(msg.getFromName()) + 1))
                                && isLessForAll(msg.getFromName(), vectorJ, vectorI)) {

                            if (passMessages.add(msg)) {
                                updateClock(msg.getFromName());
                            }
                        }
                    }
                }
            }

            for (Message msg : passMessages) {
                removeFromDelayQue(msg);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return passMessages.toArray(new Message[]{});
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

    /**
     * From the algorithm, it determines if the process from the received message,
     * knows of any more messages than this current process does.
     * @param fromName the senders name
     * @param vectorJ the senders process vector clock
     * @param vectorI current process vector clock
     * @return true if current process has received all messages before this else false
     */
    private boolean isLessForAll(String fromName, HashMap<String, Integer> vectorJ,
                                 HashMap<String, Integer> vectorI) {

        String[] keys = vectorI.keySet().toArray(new String[]{});
        for (String key : keys) {
            if (!key.equals(fromName)) {
                if (!(vectorJ.get(key) <= vectorI.get(key))) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Increments the clock for given name
     * @param fromName the name that should be incremented in the clock
     */
    protected void updateClock(String fromName) {
        if(!vectorClock.containsKey(fromName)) {
            vectorClock.put(fromName,0);
        }
        int time = vectorClock.get(fromName) + 1;
        vectorClock.put(fromName, time);
    }

    protected void setClockForName(String name,int time) {
        vectorClock.put(name,time);
    }


    protected void addToDelayQue(Message m) {
        holdQue.add(m);
    }

    protected void removeFromDelayQue(Message m) {
        holdQue.remove(m);
    }
}
