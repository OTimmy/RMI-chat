package rmi.debugservice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Created by c12ton on 10/18/16.
 */
public interface VectorContainer extends Remote{

    String getGroupName() throws RemoteException;
    String getName() throws RemoteException;

    HashMap<String,Integer> getVectorClock() throws RemoteException;

}
