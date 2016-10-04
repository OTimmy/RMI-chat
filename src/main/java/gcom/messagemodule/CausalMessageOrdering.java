package gcom.messagemodule;

import gcom.status.Status;

/**
 * Created by timmy on 03/10/16.
 */
public class CausalMessageOrdering implements MessageOrdering{
    public CausalMessageOrdering() {

    }
    public Status sendMessage(String[] memberIDs, Message m) {
        return null;
    }

    public Message convertToMessage(String[] membersName, String msg) {
        return null;
    }
}
