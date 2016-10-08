package gcom;

import gcom.communicationmodule.NonReliableCommunication;
import gcom.communicationmodule.QueCommunicationRMI;
import gcom.groupmodule.GroupMember;
import gcom.messagemodule.Message;
import gcom.nameservice.NameService;
import gcom.nameservice.NameServiceInterFace;
import gcom.observer.Observer;
import gcom.status.GCOMException;
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
public class GCOMTest {

    private static Registry registry;
    private static boolean setUpIsDone = false;

    private final String GROUP_NAME = "Group 1";
    private final String GROUP_USER_NAME = "User 1";

    private NameServiceInterFace nameService;

    private QueCommunicationRMI fakeInMessage;

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
        GroupMember member = new GroupMember(GROUP_USER_NAME,GROUP_NAME,
                              NonReliableCommunication.class.getName());
        nameService.registerGroup(GROUP_NAME,member);
        fakeInMessage = new QueCommunicationRMI();

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
    public void testCreateGroupAndSendMessage() throws GCOMException {
        try {
            String groupName = "MyGroup!";
            String username = "MeTheUser";

            GCOM gcom = new GCOM(null);

            Observer observer = new Observer() {
                @Override
                public void update() {

                }
            };

            gcom.createGroup(groupName,username,
                             NonReliableCommunication.class.getName());


            String myMessage = "Hello user!";
            gcom.sendMessageToGroup(myMessage);


//            gcom.createGroup();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testCreateGrupAndJoinGroupAndSend() {

    }


    @Test(timeout=1000)
    public void testSendMessage() throws RemoteException, NotBoundException, GCOMException, InterruptedException {
        String username = "BigRed";
        GCOM gcom = new GCOM(null);

        gcom.connectToGroup(GROUP_NAME,username);
        String myMessage = "Hej user";
        String mySecondMessage ="Hej User2";
        gcom.sendMessageToGroup(myMessage);
        gcom.sendMessageToGroup(mySecondMessage);

        fakeInMessage.waitForChatMessage();
        Message mFake = fakeInMessage.getMessage();

        assertEquals(mFake.getChatMessage(),myMessage);
        fakeInMessage.waitForChatMessage();
        mFake = fakeInMessage.getMessage();
        assertEquals(mFake.getChatMessage(),mySecondMessage);
    }

    @Test
    public void receiveMessage() throws RemoteException, NotBoundException, GCOMException, InterruptedException {
        String username = "BigRed";
        String myMessage = "Hej " + GROUP_USER_NAME;
        final GCOM gcom = new GCOM(null);

        final String[] retrivedMessage = {null};
        Observer obReceiving = new Observer() {
            @Override
            public void update() {
                try {
                    retrivedMessage[0] = gcom.getMessage().getChatMessage();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };

        gcom.registerObservers(obReceiving);
        gcom.connectToGroup(GROUP_NAME,username);
        //add observer

        gcom.sendMessageToGroup(myMessage);

        //Giving the consumer thread a chance to finnish
        Thread.sleep(100);
        assertEquals(myMessage,retrivedMessage[0]);
    }

}