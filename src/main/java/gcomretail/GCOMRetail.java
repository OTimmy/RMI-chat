package gcomretail;

import gcom.AbstractGCOM;
import gcom.communicationmodule.Communication;
import gcom.communicationmodule.CommunicationFactory;
import gcom.communicationmodule.NonReliableCommunication;
import gcom.groupmodule.GroupManager;
import gcom.groupmodule.Member;
import gcom.groupmodule.Properties;
import gcom.messagemodule.CausalMessageOrdering;
import gcom.messagemodule.Message;
import gcom.messagemodule.MessageOrdering;
import gcom.messagemodule.UnorderedMessageOrdering;
import gcom.observer.Observer;
import gcom.observer.ObserverEvent;
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
 * This class implements all the modules, hand handle the communication
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
    private final Object lockIsConActive  = new Object();

    private Thread threadProducer;
    private Thread threadConsumer;

    private boolean producerThreadActive;
    private boolean consumerThreadActive;


    public GCOMRetail(String host) throws RemoteException,NotBoundException {
        super(host);
    }

}
