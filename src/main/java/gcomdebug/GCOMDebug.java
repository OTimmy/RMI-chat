package gcomdebug;

import gcom.AbstractGCOM;
import gcom.message.Message;
import gcom.observer.Observer;
import gcom.observer.ObserverEvent;
import gcom.observer.Subject;
import gcom.status.GCOMException;
import rmi.RMIService;
import rmi.debugservice.DebugService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 10/12/16.
 */
public class GCOMDebug extends AbstractGCOM implements Observer{
    private DebugService debugService;
    public GCOMDebug(String host) throws RemoteException, NotBoundException {
        super(host);
        debugService = RMIServiceServer.getDebugService(host);


    }

    @Override
    protected Observer createCommunicationObs() {

        Observer ob = new Observer() {
            @Override
            public void update(ObserverEvent e, Message m) throws RemoteException, GCOMException {
                if (e == ObserverEvent.RECEIVED_MESSAGE) {
                    debugService.addMessage(m);
                }
            }
        };
        return  ob;
    }

    @Override
    public void update(ObserverEvent e, Message t) throws RemoteException, GCOMException {
        communication.putMessage(t);
    }
}
