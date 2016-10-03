package gcom.communicationmodule;

import gcom.observer.Subject;
import gcom.status.Status;

/**
 * Created by c12ton on 2016-09-30.
 */
public class NonReliableCommunication implements Communication,Subject{
    public Status sendMessage(String message) {
        return null;
    }
}
