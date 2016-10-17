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
    private int receivedFromMySelf = 0;
    private int myTime = 0;

    public CausalOrdering(String name) {
        this.name = name;
        vectorClock = new HashMap<>();
        delayQue = new ArrayList<>();
        vectorClock.put(name,0);
    }

    @Override
    public void setMessageStamp(Message m) {
        //if member not exist add to hashmap

        try {
            if(!vectorClock.containsKey(m.getFromName())) {
                vectorClock.put(m.getFromName(),0);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        int time = vectorClock.get(name)+1;
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
            if(!vectorClock.containsKey(m.getFromName())) {
                vectorClock.put(m.getFromName(),0);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        System.out.println("--------------reciving----------");

        printVector();
        System.out.println("................................");

        HashSet<Message> passMessages = new HashSet<>();
        ArrayList<Message> myMessags = new ArrayList<>();
        try {
            HashMap<String,Integer> vectorI = vectorClock;
            delayQue.add(m);
            //loop all messages again
            for(int i = 0; i < delayQue.size(); i++) {
                for(Message msg:delayQue) {
                    HashMap<String,Integer> vectorJ = msg.getVectorClock();
                    timeJ = vectorJ.get(msg.getFromName());

                    if(m.getFromName().equals(name)) {
                        if(timeJ == myTime+1) {
                            if(passMessages.add(msg)) {
                                myTime++;
                                myMessags.add(msg);
                            }
                        }

                    } else {
                        if((timeJ == (vectorI.get(msg.getFromName())+1))
                                && isLessForAll(msg.getFromName(),vectorJ,vectorI)) {

                            if(passMessages.add(msg)) {
                                myMessags.add(msg);
                                updateClock(msg.getFromName());

                            }
                        }
                    }
                    }
                }

            for(Message msg:myMessags) {
                System.out.println("Removing item from que: " + msg.getFromName());
                delayQue.remove(msg);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }


        return myMessags.toArray(new Message[]{});
    }


    private boolean isLessForAll(String fromName, HashMap<String,Integer> vectorJ,
                                 HashMap<String,Integer> vectorI) {

        String[] keys = vectorJ.keySet().toArray(new String[]{});
        for(String key:keys) {
            if(!key.equals(fromName)) {
                if(!(vectorJ.get(key) <= vectorI.get(key))) {
                    System.out.println("Failed isLess");
                    return  false;
                }
            }
        }

        return true;
    }

    private void updateClock(String fromName) {
        int time = vectorClock.get(fromName) + 1;
        vectorClock.put(fromName,time);
    }

    private void printVector() {
        String[] keys = vectorClock.keySet().toArray(new String[]{});
        for(String key:keys) {
            System.out.println("user: " +key +"value:" + vectorClock.get(key));
        }
    }



//    private void updateVectorClock()
}
