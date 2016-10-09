package gcom.observer;

import gcom.messagemodule.Message;

/**
 * Created by c12ton on 9/29/16.
 */
public interface Observer {
    void update(ObserverEvent e,Message m) ;
}
