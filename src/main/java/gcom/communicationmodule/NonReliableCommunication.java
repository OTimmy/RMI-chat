package gcom.communicationmodule;

import gcom.groupmodule.Member;
import gcom.message.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by c12ton on 2016-09-30.
 * An unreliable communication implementation,
 * it does not care for if message has been received or not.
 */
public class NonReliableCommunication extends Communication{

    public NonReliableCommunication()  {
        inMessages = new LinkedBlockingDeque<>();
    }

    @Override
    public void putMessage(Message m) {
        inMessages.add(m);
    }

    /**
     * Non reliable multi cast.
     * @param members
     * @param message
     * @throws RemoteException
     * @throws NotBoundException
     * @throws InterruptedException
     */
    @Override
    public void sendMessage(Member[] members, Message message)  {

        for(Member m:members) {
            try {

                message.setToName(m.getName());
                Message cloneMessage = (Message) message.clone();

//                Message copy = cloneMessage(clo);
                m.sendMessage(cloneMessage);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }
}
