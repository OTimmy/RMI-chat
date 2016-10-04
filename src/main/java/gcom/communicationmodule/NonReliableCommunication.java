package gcom.communicationmodule;

import gcom.messagemodule.Message;
import gcom.observer.Observer;
import gcom.observer.Subject;
import gcom.status.Status;

/**
 * Created by c12ton on 2016-09-30.
 */
public class NonReliableCommunication implements Communication,Subject{
    public Status sendMessage(String message, String[] membersName, Message message1) {
        return null;
    }

    public void registerObservers(Observer... obs) {

    }

    public void notifyObserver() {

    }
}
