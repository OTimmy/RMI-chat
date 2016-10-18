package rmi.debugservice;

import gcom.message.Message;

import java.util.ArrayList;

/**
 * Created by c12ton on 10/18/16.
 */
public class DelayData implements DelayContainer {
    private String groupName;
    private String name;

    private ArrayList<Message> delay;

    public DelayData(String groupName, String name, ArrayList<Message> delay) {
        this.groupName = groupName;
        this.name = name;
        this.delay = delay;

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getGroupName() {
        return groupName;
    }

    @Override
    public ArrayList<Message> getDelayQue() {
        return delay;
    }
}
