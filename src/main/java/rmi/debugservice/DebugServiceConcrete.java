package rmi.debugservice;

import gcom.message.Message;
import gcom.observer.Observer;
import gcom.observer.ObserverEvent;
import gcom.observer.Subject;
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
    private ArrayList<Observer> communicationObservers;
    private ConcurrentHashMap<String,LinkedBlockingDeque<Message>> inMessages;

    public DebugServiceConcrete() throws RemoteException {
        inMessages = new ConcurrentHashMap<>();
        communicationObservers = new ArrayList<>();
    }

    @Override
    public void addMessage(Message m) {
        inMessages.get(m.getGroupName()).add(m);
        notifyObserverControllerMessage(m);
    }

    @Override
    public void passMessage(String name, int index) throws GCOMException, RemoteException {
        Message[] messages = inMessages.get(name).toArray(new Message[]{});
        notifyObserverCommunicators(messages[index]);
    }

    @Override
    public void updateVectorClock(String name, HashMap<String, Integer> vectorClock) {
        //copy raw data

        HashMap<String,Integer> vec = new HashMap<>();
        String[] keys = vectorClock.keySet().toArray(new String[]{});
        for(String key:keys) {
            vec.put(key,vectorClock.get(key));
        }

        notifyObserverControllerVector(name,vec);

    }

    @Override
    public void registerCommunicationObserver(Observer b) {
        communicationObservers.add(b);
    }

    @Override
    public void registerControllerObserverMessage(Observer b) {
        controllerObserverMessage = b;

    }

    @Override
    public void registerControllerObserverVector(Observer b) {
        controllerObserverVector = b;
    }


    private void notifyObserverCommunicators(Message m) throws GCOMException, RemoteException {
        for(Observer ob:communicationObservers) {
            ob.update(ObserverEvent.RECEIVED_MESSAGE,m);
        }
    }

    private void notifyObserverControllerVector(String name, HashMap<String, Integer> vectorClock) {
        try {
            controllerObserverVector.update(ObserverEvent.DEBUG_GUI,vectorClock);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (GCOMException e) {
            e.printStackTrace();
        }
    }

    private void notifyObserverControllerMessage(Message m) {
        try {
            controllerObserverMessage.update(ObserverEvent.DEBUG_GUI,m);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (GCOMException e) {
            e.printStackTrace();
        }
    }

}
