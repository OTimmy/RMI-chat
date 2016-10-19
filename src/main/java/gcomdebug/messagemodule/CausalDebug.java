package gcomdebug.messagemodule;

import gcom.message.Message;
import gcom.messagemodule.CausalOrdering;
import rmi.RMIServer;
import rmi.debugservice.DebugService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by c12ton on 10/15/16.
 */
public class CausalDebug extends CausalOrdering {

    private DebugService debugService;
    private String name;
    private String groupName;
    public CausalDebug(String hostName,String groupName, String name) {
        super(name);
        this.name = name;
        this.groupName = groupName;
        try {
            debugService = RMIServer.getDebugService(hostName);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setClockForName(String name, int time) {
        super.setClockForName(name, time);
        try {
            debugService.updateVectorClock(groupName,name, (HashMap<String, Integer>) vectorClock.clone());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void addToDelayQue(Message m) {
        super.addToDelayQue(m);
        try {
            System.out.println("Added message from: " +m.getFromName());
            debugService.updateDelayQue(groupName,name, (ArrayList<Message>) holdQue.clone());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void removeFromDelayQue(Message m) {
        super.removeFromDelayQue(m);
        try {
            System.out.println("Deleted message from: " +m.getFromName());
            debugService.updateDelayQue(groupName,name, (ArrayList<Message>) holdQue.clone());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
