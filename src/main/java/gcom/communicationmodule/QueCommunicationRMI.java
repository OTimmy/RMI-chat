package gcom.communicationmodule;

import gcom.messagemodule.Message;
import gcom.status.GCOMException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by c12ton on 2016-10-06.
 */
public class QueCommunicationRMI extends UnicastRemoteObject implements QueCommunication {
    private Message currentMessage;
    private BlockingQueue<Message> chatMessageQue;
    private BlockingQueue<Message> errMessageQue;
    public QueCommunicationRMI() throws RemoteException {
        chatMessageQue = new LinkedBlockingQueue<>();
        errMessageQue = new LinkedBlockingQueue<>();
    }

    @Override
    public void putChatMessage(Message m) throws RemoteException, InterruptedException {
        chatMessageQue.put(m);
    }

    @Override
    public void putErrMessage(String err) throws RemoteException{

    }

    @Override
    public void waitForChatMessage() throws RemoteException, InterruptedException {
        currentMessage = chatMessageQue.take();
    }

    @Override
    /**
     * Only called when waitForChatMessage is done.
     */
    public Message getMessage() throws RemoteException, GCOMException, InterruptedException {
        return currentMessage;
    }
}
