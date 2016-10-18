package rmi.debugservice;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * Created by c12ton on 10/18/16.
 */
public class VectorData  extends  UnicastRemoteObject implements VectorContainer {

    private String groupName;
    private String name;
    private HashMap<String,Integer> vector;

    public VectorData(String groupName, String name, HashMap<String,Integer> vector) throws RemoteException {
        super();
//        super();
        this.groupName = groupName;
        this.name = name;
        this.vector = vector;

    }

    @Override
    public String getGroupName() throws RemoteException{
        return groupName;
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public HashMap<String, Integer> getVectorClock()  throws RemoteException{
        return vector;
    }
}
