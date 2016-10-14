package rmi.debugservice;

import gcom.observer.Observer;
import gcom.observer.Subject;

import java.rmi.Remote;

/**
 * Created by c12ton on 10/14/16.
 */
public interface DebugService extends Remote {
    Observer getMessageObserver();

//    Subject getDebug


}
