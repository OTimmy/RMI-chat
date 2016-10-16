package gcomdebug;

import gcom.AbstractGCOM;
import gcom.communicationmodule.Communication;
import gcom.communicationmodule.NonReliableCommunication;
import gcom.message.Message;
import gcom.messagemodule.Ordering;
import gcom.messagemodule.UnorderedOrdering;
import gcom.observer.Observer;
import gcom.observer.ObserverEvent;
import gcom.observer.Subject;
import gcom.status.GCOMException;
import gcomdebug.communicationmodule.NonReliableDebug;
import gcomdebug.messagemodule.UnorderedDebug;
import rmi.RMIServer;
import rmi.RMIService;
import rmi.debugservice.DebugService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 10/12/16.
 */
public class GCOMDebug extends AbstractGCOM {
    private String host;
    public GCOMDebug(String host) throws RemoteException, NotBoundException {
        super(host);
        this.host = host;
    }

    @Override
    protected Communication createCommunication(Class type) {
        if(NonReliableCommunication.class.getClass() == type) {
            return new NonReliableDebug(host);
        }
        return new NonReliableDebug(host);
    }

    @Override
    protected Ordering createOrdering(Class type, String name) {
        if(UnorderedOrdering.class.getClass() == type) {
            return new UnorderedDebug(name);
        }
        return new UnorderedDebug(name);
    }

}
