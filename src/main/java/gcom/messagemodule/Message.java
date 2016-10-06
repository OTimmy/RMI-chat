package gcom.messagemodule;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 2016-09-30.
 */
public interface Message extends Remote{

    String getMessage() throws RemoteException;

}
