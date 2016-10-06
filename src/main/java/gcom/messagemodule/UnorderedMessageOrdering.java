package gcom.messagemodule;

import java.rmi.RemoteException;

/**
 * Created by c12ton on 2016-10-04.
 */
public class UnorderedMessageOrdering implements MessageOrdering{
    @Override
    public Message convertToMessage(String[] membersName, String msg) throws RemoteException { //membersname not needed for unorderd
        ChatMessage chatMsg =  new ChatMessage(msg);
        return chatMsg;
    }

    @Override
    public void convertFromMessage(Message m) {

    }
}
