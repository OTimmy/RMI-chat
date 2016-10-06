package gcom;

import gcom.communicationmodule.NonReliableCommunication;
import gcom.communicationmodule.QueuesCommunicationRMI;
import gcom.groupmodule.GroupMember;
import gcom.messagemodule.Message;
import gcom.messagemodule.UnorderedMessageOrdering;
import gcom.nameservice.NameService;
import gcom.nameservice.NameServiceInterFace;
import gcom.observer.Observer;
import gcom.status.GCOMException;
import gcom.status.Status;
import org.junit.Before;
import org.junit.Test;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.*;

/**
 * Created by c12ton on 2016-10-04.
 */
public class GCOMTest {

    private static Registry registry;
    private static boolean setUpIsDone = false;

    private final String GROUP_NAME = "Group 1";
    private final String GROUP_USER_NAME = "User 1";

    private NameServiceInterFace nameService;


    private QueuesCommunicationRMI fakeInMessage;

    //private userQue
    @Before
    public void setUp() throws Exception {
        nameService = new NameService();

        try {

            if(!setUpIsDone) {
                registry = LocateRegistry.createRegistry(1099);
                System.out.println("Sever is ready!");
            }
            registry.rebind(NameService.class.getSimpleName(), nameService);
            setUpIsDone = true;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        setUpFakeGroup();
    }

    /**
     * Setup a fake group with non-reliable communication!
     * @throws RemoteException
     */
    private void setUpFakeGroup() throws RemoteException {
        GroupMember member = new GroupMember(GROUP_USER_NAME,GROUP_NAME, NonReliableCommunication.class.getName());
        nameService.registerGroup(GROUP_NAME,member);
        fakeInMessage = new QueuesCommunicationRMI();

        //Registering users message que on rmi
        registry.rebind(GROUP_NAME+"/"+GROUP_USER_NAME, fakeInMessage);
    }


    @Test
    public void testJoinGroup() throws RemoteException, NotBoundException, GCOMException {

        String username = "BigRed";
        GCOM gcom = new GCOM(null);
        String[] membersName =  gcom.connectToGroup(GROUP_NAME,username);
        assertArrayEquals(new String[]{GROUP_USER_NAME,username},membersName);

        //Test for class typ
    }

    @Test
    public void testSendMessage() throws RemoteException, NotBoundException, GCOMException, InterruptedException {
        String username = "BigRed";
        GCOM gcom = new GCOM(null);

        gcom.connectToGroup(GROUP_NAME,username);
        String myMessage = "Hej user";
        gcom.sendMessageToGroup(myMessage);

        Message mFake = fakeInMessage.getChatMessage();

        assertEquals(mFake.getMessage(),myMessage);
    }

    @Test
    public void receiveMessage() throws RemoteException, NotBoundException, GCOMException, InterruptedException {
        String username = "BigRed";
        GCOM gcom = new GCOM(null);

        gcom.connectToGroup(GROUP_NAME,username);
        String myMessage = "Hej " + GROUP_USER_NAME;
        //add observer

        gcom.sendMessageToGroup(myMessage);
        gcom.sendMessageToGroup(myMessage+" 2");

        // create observer
        // see if obserer gets notify when a message retrieved

        // send message message

        // test receive

    }


//    private Observer createChatNotificationRecieverTestObserver() {
//        Observer ob = new Observer() {
//            @Override
//            public void update() {
//                String[] notifications = gcom.getNotificationMessages();
//            }
//        };
//
//        return  ob;
//    }


//    private Observer createChatMessageRecieveTestObserver() {
//        Observer ob = new Observer() {
//            @Override
//            public void update() {
//                String[] messages = gcom.getChatMessages();
//            }
//        };
//
//        //getMessageList
//        //compareMessageList
//
//        return ob;
//    }

}