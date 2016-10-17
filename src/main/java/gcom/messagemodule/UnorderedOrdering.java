package gcom.messagemodule;

import gcom.message.Message;

/**
 * Created by c12ton on 2016-10-04.
 */
public class UnorderedOrdering implements Ordering {

    private String name;
    private Message message;
    public UnorderedOrdering(String name) {
        this.name = name;
    }

    @Override
    public void setMessageStamp(Message message){
    }

    @Override
    public Message[] orderMessage(Message m) {
        return new Message[]{m};
    }
}
