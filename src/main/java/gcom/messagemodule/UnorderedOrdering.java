package gcom.messagemodule;

import gcom.message.Message;

/**
 * Created by c12ton on 2016-10-04.
 */
public class UnorderedOrdering implements Ordering {

    private String name;
    public UnorderedOrdering(String name) {
        this.name = name;
    }

    @Override
    public void setMessageStamp(Message message) {}

    @Override
    public void orderMessage(Message m) {}
}
