package rmi.debugservice;

import gcom.message.Message;

import java.util.ArrayList;

/**
 * Created by c12ton on 10/18/16.
 */
public interface DelayContainer {
    String getName();
    String getGroupName();

    ArrayList<Message> getDelayQue();

}
