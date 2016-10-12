package gcom.messagemodule;

import gcom.groupmodule.Member;

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
    public void setMessageStamp(Message message) {

    }

    @Override
    public void orderMessage(Message m) {

    }
}
