package rmi.debugservice;

import gcom.groupmodule.Member;
import gcom.message.Chat;
import gcom.message.Join;
import gcom.message.Message;
import gcom.message.MessageType;
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
    private Observer controllerObserDelayQue;
    private ConcurrentHashMap<String,GroupObservers> communicationObservers;
    private ConcurrentHashMap<String,LinkedBlockingDeque<Message>> inMessages;
    private HashMap<String,ArrayList<DelayContainer>> delays;


    public DebugServiceConcrete() throws RemoteException {
        inMessages = new ConcurrentHashMap<>();
        communicationObservers = new ConcurrentHashMap<>();
        delays = new HashMap<>();
    }

    @Override
    public void addMessage(Message m) throws RemoteException{
        createMessageQueForGroup(m.getGroupName());
        inMessages.get(m.getGroupName()).add(m);
        notifyObserverControllerMessage(m);
    }

    @Override
    public void passMessage(String groupName,String toName, int index) throws GCOMException, RemoteException {
        LinkedBlockingDeque<Message> messagesList = inMessages.get(groupName);

        Message[] messages = inMessages.get(groupName).toArray(new Message[]{});
        messagesList.remove(messages[index]);


        notifyObserverCommunicators(toName,messages[index]);
    }

    @Override
    public void passMessages(String groupName) throws GCOMException, RemoteException {
        LinkedBlockingDeque l = inMessages.get(groupName);
        if(l != null) {
            while(!l.isEmpty()) {
                Message m = (Message) l.pop();
                notifyObserverCommunicators(m.getToName(),m);
            }
        }
    }

    @Override
    public void updateVectorClock(String groupName, String name, HashMap<String, Integer> vectorClock) throws RemoteException {

        VectorData vectorData = new VectorData(groupName,name,vectorClock);
        notifyObserverControllerVector(vectorData);
    }

    @Override
    public void updateDelayQue(String groupName, String name, ArrayList<Message> delayQue) throws RemoteException {
        ArrayList<DelayContainer> datas = new ArrayList<>();
        for(Message m:delayQue) {
            String textMessage = "";
            DelayContainer data = new DelayData(groupName,m.getFromName(),m.getToName());
            switch(m.getMessageType()) {
                case CHAT_MESSAGE:
                    Chat chat = (Chat)m;
                    textMessage = chat.getMessage();
                    break;
                case DELETE_MESSAGE:
                    textMessage = MessageType.DELETE_MESSAGE.toString();
                    break;
                case JOIN_MESSAGE:
                    textMessage = MessageType.JOIN_MESSAGE.toString();
                    break;
                case LEAVE_MESSAGE:
                    textMessage = MessageType.LEAVE_MESSAGE.toString();
                    break;
                case ELECTION_MESSAGE:
                    textMessage = MessageType.ELECTION_MESSAGE.toString();
                    break;
            }
            data.setMessage(textMessage);
            datas.add(data);
        }

        delays.put(name,datas);
        System.out.println("Updating delay que for member: "+ name);

        notifyControllerObserDelayQue(delays);
    }

    @Override
    public void dropMessage(String groupName, int index) throws RemoteException{
        removeMessageFromIndex(groupName,index);
    }

    private void removeMessageFromIndex(String groupName, int index) {
        Message[] messages = inMessages.get(groupName).toArray(new Message[]{});
        ArrayList<Message> msgs = new ArrayList<>();
        for(int i = 0; i < messages.length; i++) {
            if(i != index) {
                msgs.add(messages[i]);
            }
        }

        LinkedBlockingDeque l = inMessages.get(groupName);
        l.clear();

        for(Message m:msgs) {
            l.add(m);
        }
    }

    private void createMessageQueForGroup(String groupName) {
        if(!inMessages.containsKey(groupName)) {
            LinkedBlockingDeque<Message> messages = new LinkedBlockingDeque<>();
            inMessages.put(groupName,messages);
        }
    }

    @Override
    public void registerCommunicationObserver(String groupName,String name, Observer b) throws RemoteException{

        if(!communicationObservers.containsKey(groupName)) {
            GroupObservers groupObservers = new GroupObservers();
            communicationObservers.put(groupName,groupObservers);
        }

        GroupObservers groupObservers = communicationObservers.get(groupName);
        groupObservers.addUser(name,b);

    }

    @Override
    public void registerControllerObserverMessage(Observer b) throws RemoteException{
        controllerObserverMessage = b;
    }

    @Override
    public void registerControllerObserverVector(Observer b) throws RemoteException{
        controllerObserverVector = b;
    }

    @Override
    public void registerControllerOutGoingMessage(Observer b) throws RemoteException {
        controllerObserDelayQue = b;
    }

    @Override
    public String[] getMemberOfGroups(String groupName) throws RemoteException {
        GroupObservers groupObservers = communicationObservers.get(groupName);
        return groupObservers.getNames();
    }

    private void notifyObserverCommunicators(String toName,Message m) throws GCOMException, RemoteException {
        Observer ob = communicationObservers.get(m.getGroupName()).getObserver(toName);
        ob.update(ObserverEvent.RECEIVED_MESSAGE,m);
    }

    private void notifyObserverControllerVector(VectorContainer container) throws RemoteException{
        try {
            controllerObserverVector.update(ObserverEvent.DEBUG_GUI,container);
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

    private void notifyControllerObserDelayQue(HashMap<String,ArrayList<DelayContainer>> delays) throws RemoteException{
        ArrayList<DelayContainer> del = new ArrayList<>();
        try {
            String[] keys = delays.keySet().toArray(new String[]{});
            for(String key:keys) {
                ArrayList<DelayContainer> temp = delays.get(key);
                for(DelayContainer d:temp) {
                    del.add(d);
                }
            }
            controllerObserDelayQue.update(ObserverEvent.DEBUG_GUI,del);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (GCOMException e) {
            e.printStackTrace();
        }
    }

}
