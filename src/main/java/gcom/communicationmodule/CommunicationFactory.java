package gcom.communicationmodule;

import gcom.GCOM;
import gcom.status.GCOMException;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 9/29/16.
 */
public final class CommunicationFactory {
    public static Communication createNonReliableCommunication() throws RemoteException, AlreadyBoundException, NotBoundException {
        return new NonReliableCommunication();
    }
    public static Communication createReliableCommunication() throws Exception {
        throw new Exception("Not implemented yet!");
    }
}
