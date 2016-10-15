package gcomdebug;

import gcom.AbstractGCOM;
import gcom.communicationmodule.Communication;
import gcom.communicationmodule.NonReliableCommunication;
import gcom.message.Message;
import gcom.messagemodule.Ordering;
import gcom.observer.Observer;
import gcom.observer.ObserverEvent;
import gcom.observer.Subject;
import gcom.status.GCOMException;
import gcomdebug.communicationmodule.NonReliableDebug;
import rmi.RMIServer;
import rmi.RMIService;
import rmi.debugservice.DebugService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 10/12/16.
 */
public class GCOMDebug extends AbstractGCOM {
    private DebugService debugService;
    private String host;
    public GCOMDebug(String host) throws RemoteException, NotBoundException {
        super(host);
        debugService = RMIServer.getDebugService(host);
        this.host = host;
    }

    @Override
    protected Communication createCommunication(Class type) {
        if(NonReliableCommunication.class.getClass() == type) {
            return new NonReliableDebug(host);
        }
        return null;
    }

    @Override
    protected Ordering createOrdering(Class type, String name) {
        return null;
    }


    //Override createMessageOrder
        // return extendedUnOrdered

    //Override createCommunicationOrder
        //return extendedUnOrdered

}
