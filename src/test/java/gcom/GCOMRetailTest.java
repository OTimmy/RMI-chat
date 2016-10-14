package gcom;

import gcom.communicationmodule.NonReliableCommunication;
import gcom.groupmodule.GroupManager;
import gcom.groupmodule.GroupProperties;
import gcom.groupmodule.Properties;
import gcom.messagemodule.*;
import gcom.nameservice.NameService;
import gcom.nameservice.NameServiceConcrete;
import gcom.observer.Observer;
import gcom.observer.ObserverEvent;
import gcom.status.GCOMException;
import gcomretail.GCOMRetail;
import org.junit.Before;
import org.junit.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static org.junit.Assert.*;

/**
 * Created by c12ton on 2016-10-04.
 */
public class GCOMRetailTest {

    private static Registry registry;
    private static boolean setUpIsDone = false;

    private final String FAKE_LEADER_NAME = "fake_leader";
    private final String FAKE_GROUP_NAME  = "fake_group";


    private NameService nameService;

    //private userQue
    @Before
    public void setUp() throws Exception {
        if(!setUpIsDone) {
            registry = LocateRegistry.createRegistry(1099);
            System.out.println("Sever is ready!");
        }
        setUpIsDone = true;
        nameService = new NameServiceConcrete();
        registry.rebind(NameService.class.getSimpleName(),nameService);
    }

    /**
     * Setup a fake group with non-reliable communication!
     * @throws RemoteException
     */
    private void setUpFakeGroup() throws RemoteException, NotBoundException {
        String name      = FAKE_LEADER_NAME;
        String groupName = FAKE_GROUP_NAME;

        GroupManager manager = new GroupManager(null);
        Class comtype        = NonReliableCommunication.class.getClass();
        Class msgtype        = UnorderedMessageOrdering.class.getClass();
        Properties p = new GroupProperties(comtype,msgtype,groupName);
        manager.createGroup(p,name);
    }

    @Test
    public void testJoinGroup() throws RemoteException, NotBoundException, GCOMException {
        setUpFakeGroup();

        String username = "BigRed";
        GCOMRetail gcom = new GCOMRetail(null);
        String[] membersName =  gcom.connectToGroup(FAKE_GROUP_NAME,username);
        assertArrayEquals(new String[]{FAKE_LEADER_NAME,username},membersName);
    }

    @Test
    public void testSendMessage() throws RemoteException, NotBoundException, GCOMException, InterruptedException {
        setUpFakeGroup();

        String name = "BigRed";
        GCOMRetail gcom = new GCOMRetail(null);

        final String[] retrivedMessage = new String[1];
        Observer ob = new Observer() {
            @Override
            public  void update(ObserverEvent e, Message t) throws RemoteException, GCOMException {
                    Chat m = (Chat) t;
                    System.out.println("message from observer!: " + m.getMessage());
                    retrivedMessage[0] = m.getMessage();
//                }
            }
        };

        gcom.registerObservers(ob);
        gcom.connectToGroup(FAKE_GROUP_NAME,name);
        String myMessage = "Hej!";

        Message message = new ChatMessage(name,myMessage);
        gcom.sendMessageToGroup(message);

        //wait for message
        Thread.sleep(300);
        assertEquals(retrivedMessage[0],myMessage);

    }


}