package gcom.communicationmodule;

import gcom.GCOM;
import gcom.status.GCOMException;

/**
 * Created by c12ton on 9/29/16.
 */
public final class CommunicationFactory {
    public static Communication createNonReliableCommunication() {
        return new NonReliableCommunication();
    }
    public static Communication createReliableCommunication() throws Exception {
        throw new Exception("Not implemented yet!");
    }
}
