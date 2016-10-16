package rmi.debugservice;

import gcom.message.Message;
import gcom.observer.Observer;
import gcom.observer.ObserverEvent;
import gcom.status.GCOMException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by c12ton on 10/14/16.
 */
public class DebugServiceConcrete extends UnicastRemoteObject implements DebugService{

    private Observer controllerObserverVector;
    private Observer controllerObserverMessage;
    private ConcurrentHashMap<String,ArrayList<Observer>> communicationObservers;
    private ConcurrentHashMap<String,LinkedBlockingDeque<Message>> inMessages;

    public DebugServiceConcrete() throws RemoteException {
        inMessages = new ConcurrentHashMap<>();
        communicationObservers = new ConcurrentHashMap<>();
    }

    @Override
    public void addMessage(Message m) throws RemoteException{
        System.out.println("I got a message from a group :" + m.getGroupName());
        createMessageQueForNonExistingGroup(m.getGroupName());
        inMessages.get(m.getGroupName()).add(m);
        notifyObserverControllerMessage(m);
    }

    @Override
    public void passMessage(String groupName, int index) throws GCOMException, RemoteException {
        LinkedBlockingDeque<Message> messagesList = inMessages.get(groupName);
        Message[] messages = inMessages.get(groupName).toArray(new Message[]{});
        messagesList.remove(messages[index]);
        notifyObserverCommunicators(messages[index]);
    }

    @Override
    public void passMessages(String groupName) throws GCOMException, RemoteException {
        LinkedBlockingDeque l = inMessages.get(groupName);
        while(!l.isEmpty()) {
            Message m = (Message) l.pop();
            notifyObserverCommunicators(m);
        }
    }

    @Override
    public void updateVectorClock(String name, HashMap<String, Integer> vectorClock) throws RemoteException {
        HashMap<String,Integer> vec = new HashMap<>();
        String[] keys = vectorClock.keySet().toArray(new String[]{});
        for(String key:keys) {
            vec.put(key,vectorClock.get(key));
        }

        notifyObserverControllerVector(name,vec);

    }
    private void createMessageQueForNonExistingGroup(String groupName) {
        if(!inMessages.containsKey(groupName)) {
            LinkedBlockingDeque<Message> messages = new LinkedBlockingDeque<>();
            inMessages.put(groupName,messages);
        }
    }

    private void createCommunicationListForNonExisting(String groupName) {
        if(!communicationObservers.containsKey(groupName)) {
            ArrayList<Observer> observers = new ArrayList<>();
            communicationObservers.put(groupName,observers);
        }
    }

    @Override
    public void registerCommunicationObserver(String groupName, Observer b) throws RemoteException{
        communicationObservers.get(groupName).add(b);
    }

    @Override
    public void registerControllerObserverMessage(Observer b) throws RemoteException{
        controllerObserverMessage = b;

    }

    @Override
    public void registerControllerObserverVector(Observer b) throws RemoteException{
        controllerObserverVector = b;
    }


    private void notifyObserverCommunicators(Message m) throws GCOMException, RemoteException {
        Observer[] obs = communicationObservers.get(m.getGroupName()).toArray(new Observer[]{});
        for(Observer ob:obs) {
            ob.update(ObserverEvent.RECEIVED_MESSAGE,m);
        }
    }

    private void notifyObserverControllerVector(String name, HashMap<String, Integer> vectorClock) throws RemoteException{
        try {
            controllerObserverVector.update(ObserverEvent.DEBUG_GUI,vectorClock);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (GCOMException e) {
            e.printStackTrace();
        }
    }

    private void notifyObserverControllerMessage(Message m) throws RemoteException{
        try {
            controllerObserverMessage.update(ObserverEvent.DEBUG_GUI,m);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (GCOMException e) {
            e.printStackTrace();
        }
    }

}
