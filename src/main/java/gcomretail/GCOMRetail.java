package gcomretail;

import gcom.AbstractGCOM;
import gcom.communicationmodule.Communication;
import gcom.communicationmodule.CommunicationFactory;
import gcom.communicationmodule.NonReliableCommunication;
import gcom.message.Message;
import gcom.messagemodule.CausalOrdering;
import gcom.messagemodule.Ordering;
import gcom.messagemodule.UnorderedOrdering;
import gcom.observer.ObserverEvent;
import gcom.observer.Observer;
import gcom.status.GCOMException;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 9/29/16.
 * This is class implements all the modules, hand handle the communication
 * between them. And
 */
public class GCOMRetail extends AbstractGCOM{


    public GCOMRetail(String host) throws RemoteException,NotBoundException {
        super(host);
    }

    /**
     * @param name the name of the sender. That's the current user.
     * @param type
     * @return
     */
    @Override
    protected Ordering createOrdering(Class type, String name) {
        if(CausalOrdering.class.getClass() == type) {
            return new CausalOrdering(name);
        }

        return new UnorderedOrdering(name);
    }

    /**
     * Creates a communcation with repsect to given type.
     * And add appropiate listeners and observers
     * @param type of communication
     * @return
     */
    @Override
    protected Communication createCommunication(Class type) throws RemoteException, AlreadyBoundException, NotBoundException {
        if(NonReliableCommunication.class.getClass() == type) {
            return CommunicationFactory.createNonReliableCommunication();
        }

        return null;
    }

}
