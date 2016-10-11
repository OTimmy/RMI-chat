package gcom.messagemodule;

import gcom.groupmodule.Member;
import gcom.status.Status;

import java.rmi.RemoteException;

/**
 * Created by c12ton on 2016-10-04.
 */
public class UnorderedMessageOrdering implements MessageOrdering{

    private String name;
    public UnorderedMessageOrdering(String name) {
        this.name = name;
    }


    @Override
    public Message convertToMessage(Member[] members, String msg) throws RemoteException {
        return  new ChatMessage(name,msg);
    }

    @Override
    public void convertFromMessage(Message m) {

    }
}
