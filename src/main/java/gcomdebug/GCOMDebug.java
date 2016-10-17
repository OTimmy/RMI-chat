package gcomdebug;

import gcom.AbstractGCOM;
import gcom.communicationmodule.Communication;
import gcom.communicationmodule.NonReliableCommunication;
import gcom.groupmodule.Member;
import gcom.message.Message;
import gcom.messagemodule.CausalOrdering;
import gcom.messagemodule.Ordering;
import gcom.messagemodule.UnorderedOrdering;
import gcom.observer.Observer;
import gcom.observer.ObserverEvent;
import gcom.observer.Subject;
import gcom.status.GCOMException;
import gcomdebug.communicationmodule.NonReliableDebug;
import gcomdebug.messagemodule.CausalDebug;
import gcomdebug.messagemodule.UnorderedDebug;
import rmi.RMIServer;
import rmi.RMIService;
import rmi.debugservice.DebugService;

import java.rmi.AlreadyBoundException;
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
    protected Communication createCommunication(String type, String groupName, String name) throws RemoteException, AlreadyBoundException, NotBoundException {
        return new NonReliableDebug(host,groupName,name);

    }

    @Override
    protected Ordering createOrdering(String type, String groupName, String name) {
        if(CausalOrdering.class.getName().equals(type)) {
            return new CausalDebug(name,host);
        }

        return new UnorderedDebug(name);
    }

}
