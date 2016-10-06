package gcom.communicationmodule;

import gcom.GCOM;
import gcom.status.GCOMException;

import java.rmi.RemoteException;

/**
 * Created by c12ton on 9/29/16.
 */
public final class CommunicationFactory {
    public static Communication createNonReliableCommunication(String host,String groupName,String userName) throws RemoteException {
        return new NonReliableCommunication(host,groupName,userName);
    }
    public static Communication createReliableCommunication() throws Exception {
        throw new Exception("Not implemented yet!");
    }
}
