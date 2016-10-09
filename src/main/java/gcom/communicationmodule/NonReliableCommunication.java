package gcom.communicationmodule;

import gcom.groupmodule.Member;
import gcom.messagemodule.Message;
import gcom.status.GCOMException;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by c12ton on 2016-09-30.
 * An unreliable communication implementation,
 * it does not care for if message has been received or not.
 */
public class NonReliableCommunication implements Communication{


    private BlockingDeque<Message> inMessages;
    private Message currentMessage;
    private String host;
    private String groupName;

    public NonReliableCommunication() throws RemoteException, AlreadyBoundException, NotBoundException {
        inMessages = new LinkedBlockingDeque<>();
    }

    @Override
    public void putMessage(Message m) {
        inMessages.add(m);
    }

    @Override
    public void sendMessage(Member[] members, Message message) throws RemoteException, NotBoundException, InterruptedException {
        for(Member m:members) {
            m.sendMessageToMember(message);
        }
    }

    @Override
    public void waitForMessage() throws RemoteException, InterruptedException {
        currentMessage = inMessages.take();
    }

    @Override
    public Message getMessage() throws RemoteException, GCOMException, InterruptedException {
        return currentMessage;
    }
}
