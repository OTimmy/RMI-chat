package gcom.communicationmodule;

import gcom.groupmodule.Member;
import gcom.message.Message;
import gcom.status.GCOMException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by c12ton on 2016-09-30.
 */
public abstract class Communication {
    protected BlockingDeque<Message> inMessages;

    public Communication() {
        inMessages  = new LinkedBlockingDeque<>();
    }

    public abstract void putMessage(Message m);
    public   abstract void sendMessage(Member[] members, Message message) throws RemoteException
                                                                                  ,NotBoundException
                                                                                  ,InterruptedException;

    /**
     * @return
     * @throws RemoteException
     * @throws GCOMException
     * @throws InterruptedException
     */
    public Message getMessage() throws RemoteException, GCOMException, InterruptedException {
      return  inMessages.take();
    }
}
