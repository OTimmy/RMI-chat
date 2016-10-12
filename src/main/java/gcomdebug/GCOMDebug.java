package gcomdebug;

import gcom.AbstractGCOM;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 10/12/16.
 */
public class GCOMDebug extends AbstractGCOM{
    public GCOMDebug(String host) throws RemoteException, NotBoundException {
        super(host);
    }
}
