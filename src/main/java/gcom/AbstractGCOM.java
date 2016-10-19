package gcom;

import gcom.communicationmodule.Communication;
import gcom.communicationmodule.CommunicationFactory;
import gcom.communicationmodule.NonReliableCommunication;
import gcom.groupmodule.*;
import gcom.message.*;
import gcom.messagemodule.*;
import gcom.observer.ObserverEvent;
import gcom.observer.Observer;
import gcom.observer.Subject;
import gcom.status.GCOMError;
import gcom.status.GCOMException;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by c12ton on 9/29/16.
 * This class implements all the modules, hand handle the communication
 * between them. And
 */
public abstract class AbstractGCOM implements Subject, Observer{

    //The modules
    protected GroupManager groupManager;
    protected Ordering messageOrdering;
    protected Communication communication;

    private ArrayList<Observer> observers;

    private BlockingQueue<Message> outgoingChatMessage;

    private final Object lockIsProdActive = new Object();
    private final Object lockIsConActive  = new Object();

    private Thread threadProducer;
    private Thread threadConsumer;

    private boolean producerThreadActive;
    private boolean consumerThreadActive;


    public AbstractGCOM(String host) throws RemoteException,NotBoundException {
        outgoingChatMessage  = new LinkedBlockingQueue<>();
        observers            = new ArrayList<>();
        groupManager         = new GroupManager(host);

        producerThreadActive = true;
        consumerThreadActive = true;

        threadProducer = createProducerThread();
        threadConsumer = createConsumerThread();


        groupManager.registerObservers(createObserverMemberLeave());
        groupManager.registerObservers(createCommunicationObs());
        //groupManager.registerMemberOserver()
        //groupManager.registerCommunicationObserver(communication)
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
            //copy stuff just in case.
            Properties p = groupManager.getGroupProperties(groupName);
            Properties properties = new GroupProperties(p.getComtype(),
                                                        p.getMessagetype(),
                                                        p.getGroupName());

            messageOrdering = createOrdering(properties.getMessagetype(),
                                             p.getGroupName(),name);
            communication   = createCommunication(properties.getComtype(),
                                                  p.getGroupName(),name);

            //connect to group
            Member[] members = groupManager.joinGroup(properties,name);

            for(Member m:members) {
                membersNames.add(m.getName());
            }

        } catch (GCOMException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GCOMException(GCOMError.CANT_CONNECT_TO_GROUP);
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
                Message m = (Message) t;
                if(e == ObserverEvent.RECEIVED_MESSAGE) {
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

            communication   = createCommunication(p.getComtype(),p.getGroupName(),name);
            messageOrdering = createOrdering(p.getMessagetype(),p.getGroupName(),name);
            groupManager.createGroup(p,name);

        } catch (Exception e) {
            throw new GCOMException(e.toString());
        }

        threadConsumer.start();
        threadProducer.start();
    }


    /**
     * @param m the message to be sent to the current group, will be
     *          placed in a pending que.
     */
    public void sendMessageToGroup(Message m) throws RemoteException {
        try {
            String groupName = groupManager.getProperties().getGroupName();
            m.setGroupName(groupName);
            m.setFromName(groupManager.getName());
            outgoingChatMessage.put(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used by the
     * @return
     */
    private Observer createObserverMemberLeave() {
        Observer ob = new Observer() {
            @Override
            public<T> void update(ObserverEvent e,T t) throws RemoteException, GCOMException {
                Message m = (Message) t;
                if(e == ObserverEvent.MESSAGE_TO_GROUP) {
                    sendMessageToGroup(m);
                }
            }
        };

        return ob;
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

                    Message message  = outgoingChatMessage.take();
                    Member[] members = groupManager.getMembers();
                    String[] names = new String[members.length];

                    for(int i = 0; i < members.length; i++) {
                        names[i] = members[i].getName();
                    }

                    messageOrdering.setMessageStamp(message,names);
                    communication.sendMessage(members, message);
                } catch (InterruptedException e) {
//                    e.printStackTrace();
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

                    Message message = communication.getMessage();

                    if(message.getMessageType() == MessageType.JOIN_MESSAGE) {
                        Join m = (Join) message;
                        if(m.getName().equals(groupManager.getName())) {
                            messageOrdering.setVectorClock(message.getVectorClock(),message.getFromName());
                        }
                    }

                    Message[] messages = messageOrdering.orderMessage(message);

                    for(Message m:messages) {
                        handleMessageType(m);
                        notifyObserver(ObserverEvent.CHAT_MESSAGE,m);
                    }

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

    /**
     * Handles each message type in it's each respective way.
     * @param m
     */
    private void handleMessageType(Message m) {
        try {
            MessageType type = m.getMessageType();

            switch(type) {
                case JOIN_MESSAGE:
                    Join j = (Join) m;
                    groupManager.addMember(j.getMember());
                    break;
                case LEAVE_MESSAGE:
                    Leave l = (Leave) m;
                    groupManager.removeMember(l.getName());
                    //remove clock from messageOrdering
                    break;
                case ELECTION_MESSAGE:
                    Election e = (Election) m;
                    groupManager.setLeader(e.getLeader());
                    break;
                case DELETE_MESSAGE:
                    stopProducerThread();
                    stopConsumerThread();
                    groupManager.removeGroup();
                    groupManager    = null;
                    messageOrdering = null;
                    communication   = null;
                    break;
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * As a leader, delet the current group
     */
    public void leaveGroup() {
        groupManager.leaderDeleteGroup();
    }


    public void stopProducerThread() {
        synchronized (lockIsProdActive) {
            producerThreadActive = false;
            threadProducer.interrupt();
        }
    }

    public void stopConsumerThread() {
        synchronized (lockIsConActive) {
            consumerThreadActive = false;
            threadConsumer.interrupt();
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

    protected abstract Communication createCommunication(String type,String groupName,String name) throws RemoteException, AlreadyBoundException, NotBoundException;

    protected abstract Ordering createOrdering(String type, String groupName,String name);


    @Override
    public <T> void update(ObserverEvent e, T t) throws RemoteException, GCOMException {

        //if instance of groupManager
            //member has left
            //sendMessageToGroup(Message)
        //if instance of member
            //received message
            //communication(message)

    }

    /**
     * Register observers
     * @param obs
     */
    public void registerObservers(Observer... obs) {
        for(Observer ob:obs) {
            observers.add(ob);
        }
    }

    @Override
    public  void notifyObserver(ObserverEvent e,Message message) throws RemoteException, GCOMException {
        for(Observer ob:observers) {
            ob.update(e,message);
        }
    }
}
