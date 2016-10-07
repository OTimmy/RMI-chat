package gcom;

import gcom.communicationmodule.Communication;
import gcom.communicationmodule.CommunicationFactory;
import gcom.communicationmodule.NonReliableCommunication;
import gcom.groupmodule.GroupMember;
import gcom.groupmodule.Member;
import gcom.messagemodule.*;
import gcom.nameservice.NameService;
import gcom.nameservice.NameServiceInterFace;
import gcom.observer.Observer;
import gcom.observer.Subject;
import gcom.status.GCOMException;
import gcom.status.Status;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by c12ton on 9/29/16.
 * This is class implements all the modules, hand handle the communication
 * between them. And
 */
public class GCOM implements Subject{

    private GroupMember member;
    private MessageOrdering messageOrdering;
    private Communication communication;
    private NameServiceInterFace nameService;
    private String host;
    private BlockingQueue<String> outgoingChatMessage;


    private final Object lockIsProdActive = new Object();
    private final Object lockIsConActive = new Object();


    private Thread threadProducer;
    private Thread threadConsumer;

    private boolean producerThreadActive;
    private boolean consumerThreadActive;

    private Observer messageObs;

    public GCOM(String host) throws RemoteException,NotBoundException {
        this.host            = host;
        nameService          = NameService.getNameService(host);
        outgoingChatMessage  = new LinkedBlockingQueue<>();

        producerThreadActive = true;
        consumerThreadActive = true;


        threadProducer = createProducerThread();
        threadConsumer = createConsumerThread();

    }

    //TODO catch exceptions here, and return status message!
    //TODO add message que for outgoing messages, for thread to handle and wait for
    public Status sendMessageToGroup(String msg) throws InterruptedException, RemoteException, NotBoundException {
        outgoingChatMessage.put(msg);
        return null;
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
                    String[] membersNames = member.getMemberNames();
                    Message  message = messageOrdering.convertToMessage(member.getName(),membersNames,msg,null);
                    communication.sendMessage(membersNames,message);
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
                    communication.waitForMessage();

//                    Message m = communication.getMessage();

//                    if(m.getStatusMessage() != null) {
//                        //member.sendStatus
//                    }
                    // if message.getStatus() != null
                        // update member
                        // notify inStatusMessage.update()
                    // if message.getChatMessage != null
                        //inChatMessage.update()
                    if(messageObs != null) {
                        messageObs.update();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return t;
    }

    /**
     *
     * @return
     * @throws RemoteException
     */
    public Message getMessage() throws RemoteException {
        Message message = null;
        try {
            message = communication.getMessage();
        } catch (GCOMException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * Try to join group trough leader of a group by given group name
     * @param groupName the group to join
     * @param username the users username for that group
     * @return a list of member names
     * @throws RemoteException
     * @throws GCOMException in that member already exists
     */
    public String[] connectToGroup(String groupName,String username) throws RemoteException, GCOMException {
        HashMap<String,Member> leaders = nameService.getGroups();
        Member leader = leaders.get(groupName);

        communication = createCommunication(leader.getCommunicationType(),groupName,username);
        messageOrdering = createMessageOrdering(UnorderedMessageOrdering.class.getName()); //todo

        member = new GroupMember(username,groupName,leader.getCommunicationType());
        leader.joinGroup(member);

        threadConsumer.start();
        threadProducer.start();

        return member.getMemberNames();
    }

    public void createGroup(String groupName, String username,String comType) {
        //nameService.createGroup(String groupName, member)
    }

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
     * @param type of
     * @return
     */
    private Communication createCommunication(String type, String groupName, String userName) throws RemoteException {
        if(type.equals(NonReliableCommunication.class.getName())) {
            return CommunicationFactory.createNonReliableCommunication(host,groupName,userName);
        }

        return null;
    }

    /**
     *
     * @param type
     * @return
     */
    private MessageOrdering createMessageOrdering(String type) {
        if(type.equals(CausalMessageOrdering.class.getName())) {
            return new CausalMessageOrdering();
        }

        return new UnorderedMessageOrdering();
    }

    /**
     * Register observers in following order: Receiving,Sending
     * @param obs
     */
    public void registerObservers(Observer... obs) {
        messageObs = obs[0];
    }

    public void notifyObserver() {

        //for (int i = 0; i <
        //for observers:
            //if messageStatus != null
                //


//        Message m = communication.getMessage();
//        if(m.getStatusMessage() != null) {
//            //member.update()
//            //statusObserver.update()
//        }

        //if chatMessage.isNotEmpy
            //  obsChatMessage.update()

        // if StatusMesage.isNotEmpty()
            // obsStatusMessage.update()

    }
}
