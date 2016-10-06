package gcom.communicationmodule;

import gcom.messagemodule.Message;
import gcom.observer.Observer;
import gcom.observer.Subject;
import gcom.status.Status;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by c12ton on 2016-09-30.
 */
public class NonReliableCommunication implements Communication,Subject{

    private String host;
    private String groupName;
    private Registry registry;
    private QueuesCommunicationRMI quesRMI;

    public NonReliableCommunication(String host, String groupName, String memberName) throws RemoteException {
        this.host = host;
        this.groupName = groupName;
        registry = LocateRegistry.getRegistry(host);
        quesRMI = new QueuesCommunicationRMI();
        registry.rebind(groupName+"/"+memberName,quesRMI);
    }

    @Override
    public Status sendMessage(String[] membersNames, Message message) throws RemoteException, NotBoundException, InterruptedException {
        for(String memberName:membersNames) {
            CommunicationRMI memRMICom = getMemberQue(memberName);
            memRMICom.putChatMessage(message);
        }
        return null;
    }

    /**
     *
     * @param memberName
     * @return
     * @throws RemoteException
     * @throws NotBoundException
     */
    private CommunicationRMI getMemberQue(String memberName) throws RemoteException, NotBoundException {
        return (CommunicationRMI) registry.lookup(groupName+"/"+memberName);
    }


    public void registerObservers(Observer... obs) throws RemoteException{

    }

    public void notifyObserver() throws RemoteException{

    }

}
