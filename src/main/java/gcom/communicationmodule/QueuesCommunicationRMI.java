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
public class QueuesCommunicationRMI extends UnicastRemoteObject implements CommunicationRMI {
    private BlockingQueue<Message> chatMessage;
    private BlockingQueue<Message> errMessage;
    public QueuesCommunicationRMI() throws RemoteException {
        chatMessage = new LinkedBlockingQueue<>();
        errMessage = new LinkedBlockingQueue<>();
    }

    @Override
    public void putChatMessage(Message m) throws RemoteException, InterruptedException {
        chatMessage.put(m);
    }

    @Override
    public void putErrMessage(String err) throws RemoteException{

    }

    @Override
    public Message getChatMessage() throws RemoteException, GCOMException, InterruptedException {
        System.out.println("HELLO WORLD!!!");
        return chatMessage.take();
    }
}
