package rmi.debugservice;

import gcom.message.Message;
import gcom.observer.Observer;
import gcom.observer.ObserverEvent;
import gcom.observer.Subject;
import gcom.status.GCOMException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by c12ton on 10/14/16.
 */
public class DebugServiceConcrete extends UnicastRemoteObject implements DebugService, Observer{


    private ConcurrentHashMap<String,ArrayList<Message>> inMessages;
    public DebugServiceConcrete() throws RemoteException {
        inMessages = new ConcurrentHashMap<>();
    }




    private Subject createSubjectMessages() {
        Subject s = new Subject() {
            private Observer b;
            @Override
            public void registerObservers(Observer... obs) throws RemoteException {
                b = obs[0];
            }

            @Override
            public void notifyObserver(ObserverEvent e, Message message) throws RemoteException, GCOMException {
                b.update(e,message);
            }
        };

        return  s;
    }

    private Observer createObserverIncomingMessages() {
        Observer ob = new Observer() {
            @Override
            public void update(ObserverEvent e, Message t) throws RemoteException, GCOMException {
//                addMessageToHold(t);
            }
        };

        return ob;
    }

    //synchronized addMessageToQue(Message m)
        //ArrayList list = inMessages.get(m.getGroup)
        //list.add(m)

    //sendMessage(Message message)
            //subjectMessages.NotifyObserver(ObserverEvent.Message,message)





    //nested class
    @Override
    public void update(ObserverEvent e, Message t) throws RemoteException, GCOMException {
        //if event == CHAT_MESSAGE

        //if event == order

        // if event == vectorClock
    }

    @Override
    public Observer getMessageObserver() {
        return null;
    }


    //nestclass for observing MessageOrdering
        //update

    //nestedClass for observing Messages
        //update


    //nestedClass for subject for communication
        //notifyObsever(ObserverEvent.MESSAGE,m)
}
