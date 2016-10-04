package gcom.messagemodule;

import gcom.status.Status;

/**
 * Created by timmy on 03/10/16.
 */
public interface MessageOrdering {
    Status sendMessage(String[] memberIDs,Message m);
}
