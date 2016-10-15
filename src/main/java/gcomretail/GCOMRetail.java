package gcomretail;

import gcom.AbstractGCOM;
import gcom.communicationmodule.Communication;
import gcom.message.Message;
import gcom.messagemodule.Ordering;
import gcom.observer.ObserverEvent;
import gcom.observer.Observer;
import gcom.status.GCOMException;

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

    @Override
    protected Communication createCommunication(Class type) {
        return null;
    }

    @Override
    protected Ordering createOrdering(Class type, String name) {
        return null;
    }
}
