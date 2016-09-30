package gcom.communicationmodule;

import gcom.status.Status;

/**
 * Created by c12ton on 2016-09-30.
 */
public interface Communication {
    Status sendMessage(String message);
}
