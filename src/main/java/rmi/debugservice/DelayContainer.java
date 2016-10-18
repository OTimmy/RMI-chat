package rmi.debugservice;

import gcom.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by c12ton on 10/18/16.
 */
public interface DelayContainer extends Remote{
    String getFromName() throws RemoteException;
    String getToName() throws RemoteException;
    String getMessage() throws RemoteException;
    void setMessage(String msg) throws RemoteException;
}
