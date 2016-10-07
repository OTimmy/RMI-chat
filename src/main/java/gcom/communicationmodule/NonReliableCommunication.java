package gcom.communicationmodule;

import gcom.messagemodule.Message;
import gcom.observer.Observer;
import gcom.observer.Subject;
import gcom.status.GCOMException;
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
    private QueCommunicationRMI quesRMI;

    public NonReliableCommunication(String host, String groupName, String memberName) throws RemoteException {
        this.host = host;
        this.groupName = groupName;
        registry = LocateRegistry.getRegistry(host);
        quesRMI = new QueCommunicationRMI();
        registry.rebind(groupName+"/"+memberName,quesRMI);
    }

    @Override
    public Status sendMessage(String[] membersNames, Message message) throws RemoteException, NotBoundException, InterruptedException {
        for(String memberName:membersNames) {
            QueCommunication memRMICom = getMemberQue(memberName);
            System.out.println("YO!!!!");
            memRMICom.putChatMessage(message);
        }
        return null;
    }

    @Override
    public void waitForMessage() throws RemoteException, InterruptedException {
        quesRMI.waitForChatMessage();
    }

    @Override
    public Message getMessage() throws RemoteException, GCOMException, InterruptedException {
        return quesRMI.getMessage();
    }

    /**
     *
     * @param memberName
     * @return
     * @throws RemoteException
     * @throws NotBoundException
     */
    private QueCommunication getMemberQue(String memberName) throws RemoteException, NotBoundException {
        return (QueCommunication) registry.lookup(groupName+"/"+memberName);
    }


    public void registerObservers(Observer... obs) throws RemoteException{

    }

    public void notifyObserver() throws RemoteException{

    }

}
