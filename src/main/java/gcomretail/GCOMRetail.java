package gcomretail;

import gcom.AbstractGCOM;
import gcom.communicationmodule.Communication;
import gcom.communicationmodule.CommunicationFactory;
import gcom.communicationmodule.NonReliableCommunication;
import gcom.groupmodule.*;
import gcom.messagemodule.*;
import gcom.observer.ObserverEvent;
import gcom.observer.Observer;
import gcom.observer.Subject;
import gcom.status.GCOMException;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by c12ton on 9/29/16.
 * This is class implements all the modules, hand handle the communication
 * between them. And
 */
public class GCOMRetail extends AbstractGCOM implements Subject{

    //The modules
    private GroupManager groupManager;
    private MessageOrdering messageOrdering;
    private Communication communication;

    private ArrayList<Observer> observers;

    private BlockingQueue<String> outgoingChatMessage;

    private final Object lockIsProdActive = new Object();
    private final Object lockIsConActive = new Object();

    private Thread threadProducer;
    private Thread threadConsumer;

    private boolean producerThreadActive;
    private boolean consumerThreadActive;


    public GCOMRetail(String host) throws RemoteException,NotBoundException {
        super(host);
        outgoingChatMessage  = new LinkedBlockingQueue<>();
        observers            = new ArrayList<>();
        groupManager         = new GroupManager(host);

        producerThreadActive = true;
        consumerThreadActive = true;

        threadProducer = createProducerThread();
        threadConsumer = createConsumerThread();
    }

    /**
     * @return a array of group names, currently at nameservice
     */
    public String[] getGroups() {
        try {
            return groupManager.getGroups();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Try to join group trough leader of a group by given group name
     * @param groupName the group to join
     * @param name the users username for that group
     * @return a list of member names
     * @throws RemoteException
     * @throws GCOMException in that member already exists
     */
    public String[] connectToGroup(String groupName,String name) throws GCOMException {

        ArrayList<String>membersNames = new ArrayList<>();

        try {
            //setup
            Properties properties = groupManager.getGroupProperties(groupName);
            messageOrdering = createMessageOrdering(properties.getMessagetype(),name);
            communication   = createCommunication(properties.getComtype());

            groupManager.registerObservers(createCommunicationObs());

            //connect to group
            Member[] members = groupManager.joinGroup(groupName,name);

            for(Member m:members) {
                membersNames.add(m.getName());
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            throw new GCOMException(e.toString());
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        threadConsumer.start();
        threadProducer.start();

        return membersNames.toArray(new String[]{});
    }


    /**
     * @return An observer for communication.
     */
    private Observer createCommunicationObs() {
        Observer ob = new Observer() {
            @Override
            public <T> void update(ObserverEvent e, T t) throws RemoteException, GCOMException {
                if(e == ObserverEvent.CHAT_MESSAGE) {
                    System.out.println("I RECEIVED A MESSAGE!");
                    Message m = (Message) t;
                    communication.putMessage(m);
                }
            }
        };
        return ob;
    }

    /**
     * Creats a group as given user as it's leader, then
     * starts the thread for conumser and producer design.
     * @param p is the properties for the group, name and types.
     * @param name the user name for the client
     * @throws GCOMException
     */
    public void createGroup(Properties p, String name) throws GCOMException {
        try {

            groupManager.createGroup(p,name);
            communication   = createCommunication(p.getComtype());
            messageOrdering = createMessageOrdering(p.getMessagetype(),name);

            groupManager.registerObservers(createCommunicationObs());

        } catch (Exception e) {
            throw new GCOMException(e.toString());
        }

        threadConsumer.start();
        threadProducer.start();
    }


    /**
     * @param msg to be sent to the current group
     */
    public void sendMessageToGroup(String msg)  {
        try {
            outgoingChatMessage.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Creats a thread for sending messages, by waiting till a message
     * is in the que.
     * @return a thread for producer
     */
   private Thread createProducerThread() {

        Thread t = new Thread(() -> {
            while(isProducerThreadActive()) {
                try {
                    String msg = outgoingChatMessage.take();
                    Member[] members = groupManager.getMembers();
                    Message  message = messageOrdering.convertToMessage(members,msg);
                    communication.sendMessage(members, message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (NotBoundException e) {
                    e.printStackTrace();
                }
            }
        });
        return t;
    }

    /**
     *Creates a thread for fetching messages
     * @return a thread for consumer
     */
    private Thread createConsumerThread() {
        Thread t = new Thread(() -> {
            while(isConsumerThreadActive()) {
                try {

                    Message m = communication.getMessage();
                    System.out.println("I have certainly recieved message!");
                    notifyObserver(ObserverEvent.RECEIVED_MESSAGE,m);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (GCOMException e) {
                    e.printStackTrace();
                }
            }
        });
        return t;
    }


    //abstract interceptor.....


    public void leaveGroup() {
        //stop producerThread()
        //stop consumerThread()
        //Remove user from registry
        //Remove communcationQue from registry
    }


    public void stopProducerThread() {
        synchronized (lockIsProdActive) {
            producerThreadActive = false;
        }
    }

    public void stopConsumerThread() {
        synchronized (lockIsConActive) {
            consumerThreadActive = false;
        }
    }

    /**
     *
     * @return
     */
    public boolean isProducerThreadActive() {
        synchronized (lockIsProdActive) {
            return producerThreadActive;
        }
    }

    /**
     *
     * @return
     */
    public boolean isConsumerThreadActive() {
        synchronized (lockIsConActive) {
            return consumerThreadActive;
        }
    }

    /**
     * Creates a communcation with repsect to given type.
     * And add appropiate listeners and observers
     * @param type of communication
     * @return
     */
    private Communication createCommunication(Class type) throws RemoteException, AlreadyBoundException, NotBoundException {
        if(NonReliableCommunication.class.getClass() == type) {
            return CommunicationFactory.createNonReliableCommunication();
        }

        return null;
    }

    /**
     * @param name the name of the sender. That's the current user.
     * @param type
     * @return
     */
    private MessageOrdering createMessageOrdering(Class type, String name) {
        if(CausalMessageOrdering.class.getClass() == type) {
            return new CausalMessageOrdering(name);
        }

        return new UnorderedMessageOrdering(name);
    }

    /**
     * Register observers in following order: Receiving,Sending
     * @param obs
     */
    public void registerObservers(Observer... obs) {
        for(Observer ob:obs) {
            observers.add(ob);
        }
    }

    @Override
    public <T> void notifyObserver(ObserverEvent e, T t) throws RemoteException, GCOMException {
        for(Observer ob:observers) {
            ob.update(e,t);
        }
    }
}
