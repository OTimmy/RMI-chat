package gcom.messagemodule;

import gcom.status.Status;

import java.rmi.RemoteException;

/**
 * Created by c12ton on 2016-10-04.
 */
public class UnorderedMessageOrdering implements MessageOrdering{

    @Override
    public Message convertToMessage(String user, String[] membersName, String msg, Status status) throws RemoteException {
        ClientMessage chatMsg =  new ClientMessage(user,msg,status);
        return chatMsg;
    }

    @Override
    public void convertFromMessage(Message m) {

    }
}
