package gcom.messagemodule;

import gcom.status.Status;

/**
 * Created by timmy on 03/10/16.
 */
public interface MessageOrdering {

    /**
     *
     * @param membersName
     * @param msg
     * @return
     */
    Message convertToMessage(String[] membersName, String msg);

    /**
     *
     */
    void convertFromMessage(Message m);
}
