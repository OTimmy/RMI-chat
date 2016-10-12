package gcom.groupmodule;

import gcom.communicationmodule.NonReliableCommunication;
import gcom.messagemodule.MemberMessage;
import gcom.messagemodule.Message;
import gcom.messagemodule.MessageType;
import gcom.messagemodule.UnorderedMessageOrdering;
import gcom.nameservice.NameService;
import gcom.nameservice.NameServiceConcrete;
import gcom.observer.Observer;
import gcom.observer.ObserverEvent;
import gcom.status.GCOMException;
import org.junit.Before;
import org.junit.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static org.junit.Assert.*;

/**
 * Created by c12ton on 10/10/16.
 */
public class GroupManagerTest {

    private final String FAKE_LEADER_NAME = "fake_leader";
    private final String FAKE_GROUP_NAME  = "fake_group";


    private static boolean setUpIsDone = false;
    private static NameService nameService;
    private static Registry registry;


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

    private void createFakeGroup() throws RemoteException, NotBoundException {
        String name      = FAKE_LEADER_NAME;
        String groupName = FAKE_GROUP_NAME;

        GroupManager manager = new GroupManager(null);
        Class comtype        = NonReliableCommunication.class.getClass();
        Class msgtype        = UnorderedMessageOrdering.class.getClass();
        Properties p = new GroupProperties(comtype,msgtype,groupName);
        manager.createGroup(p,name);
    }

    @Test
    public void testCreateGroup() throws Exception {
        String name = "user";
        String groupName = "testGroup";

        Class comtype        = NonReliableCommunication.class.getClass();
        Class msgtype        = UnorderedMessageOrdering.class.getClass();
        Properties p = new GroupProperties(comtype,msgtype,groupName);

        GroupManager manager = new GroupManager(null);
        manager.createGroup(p,name);

        Member m = nameService.getGroupLeader(groupName);
        assertEquals(m.getName(),name);

    }

    @Test
    public void joinGroup() throws Exception {
        createFakeGroup();
        String name = "user";

        GroupManager manager = new GroupManager(null);
        Member[] members = manager.joinGroup(FAKE_GROUP_NAME,name);

        assertEquals(FAKE_LEADER_NAME,members[0].getName());

    }


    @Test
    public void testSendMessage() throws Exception {
        createFakeGroup();

        String name = "user";
        String mymessage = "Hej!";
        String host = null;

        GroupManager manager = new GroupManager(host);
        final Message[] received = new Message[1];
        Observer ob = new Observer() {
            @Override
            public  void update(ObserverEvent e, Message t) throws RemoteException, GCOMException {
                if(e == ObserverEvent.CHAT_MESSAGE) {
                    received[0] = (Message) t;
                }
            }
        };
        manager.registerObservers(ob);

        Member[] members = manager.joinGroup(FAKE_GROUP_NAME,name);
        MemberMessage mymsg = new MemberMessage(name,mymessage, MessageType.CHAT_MESSAGE);

        for(Member m:members) {
            m.sendMessage(mymsg);
        }

        Thread.sleep(200);
        assertEquals(received[0].getChatMessage(),mymessage);
    }

}