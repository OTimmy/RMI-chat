package gcom.communicationmodule;

import gcom.messagemodule.Message;
import gcom.observer.Observer;
import gcom.observer.Subject;
import gcom.rmi.RMIServer;
import gcom.rmi.rmique.RMIService;
import gcom.status.GCOMException;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

/**
 * Created by c12ton on 2016-09-30.
 */
public class NonReliableCommunication implements Communication,Subject{

    private String host;
    private String groupName;
    private Registry registry;
    private RMIService rmiService;
    private QueCommunicationRMI quesRMI;

    public NonReliableCommunication(String host, String groupName, String memberName) throws RemoteException, AlreadyBoundException, NotBoundException {
        this.host = host;
        this.groupName = groupName;

        quesRMI = new QueCommunicationRMI();
        rmiService = RMIServer.getRMIService(host);
        rmiService.rebind(groupName+"/"+memberName,quesRMI);
    }

    @Override
    public void sendMessage(String[] membersNames, Message message){
        for(String memberName:membersNames) {
            QueCommunication memRMICom = null;
            try {
                memRMICom = getMemberQue(memberName);
                memRMICom.putChatMessage(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
        return (QueCommunication) rmiService.lookup(groupName+"/"+memberName);
    }

    public void registerObservers(Observer... obs) throws RemoteException{

    }

    public void notifyObserver() throws RemoteException{

    }

}
