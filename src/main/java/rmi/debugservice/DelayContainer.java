package rmi.debugservice;

import gcom.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by c12ton on 10/18/16.
 */
public interface DelayContainer extends Remote{
    String getName() throws RemoteException;
    String getGroupName() throws RemoteException;

    ArrayList<Message> getDelayQue() throws RemoteException;

}
