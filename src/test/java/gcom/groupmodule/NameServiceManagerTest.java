package gcom.groupmodule;

import gcom.communicationmodule.NonReliableCommunication;
import gcom.nameservice.NameService;
import gcom.nameservice.NameServiceInterFace;
import gcom.status.GCOMException;
import gcom.status.Status;
import junit.framework.TestSuite;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;

/**
 * Created by c12ton on 9/29/16.
 *  Test name service funcitonality with adding groups and members to them
 */
public class NameServiceManagerTest extends TestSuite {
    private static boolean setUpIsDone = false;
    private NameServiceInterFace nameService;
    private static Registry registry;


    @Before
    public void setUp() throws Exception {
        if(!setUpIsDone) {
//            NameService.main(null);
            try {
                registry = LocateRegistry.createRegistry(1099);
                nameService = new NameService();
                registry.rebind(NameService.class.getSimpleName(), nameService);
                System.out.println("Sever is ready!");
                System.out.println("Set up!");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        setUpIsDone = true;
        nameService = new NameService();
        registry.rebind(NameService.class.getSimpleName(), nameService);
    }

    @After
    public void tearDown() throws Exception {
        registry.unbind(NameService.class.getSimpleName());
    }

    @Test
    public void testGetNameService() throws RemoteException, NotBoundException {
        NameService.getNameService(null);
    }

    @Test
    public void testRegisterGroup() throws RemoteException {

        String groupName = "Group 1";
        String groupMember = "user1";
        String communicationType = "nonReliable";

        GroupMember member = new GroupMember(groupMember,groupName,
                                                communicationType);
        member.setLeader(member);

        Status status = nameService.registerGroup(groupName, member);

        System.out.println("Status: " + status.toString());

        if(status == Status.CANT_CONNECT_TO_NAME_SERVICE) {
            fail();
        }

        HashMap<String,Member> leaders = nameService.getGroups();

        assertTrue(leaders.containsKey(groupName));
    }


    @Test
    public void testJoinGroup() throws RemoteException, GCOMException {

        //setting up group
        String groupName = "Group 1";
        String groupLeader = "user1";
        String communicationType = "nonReliable";

        GroupMember member = new GroupMember(groupLeader,groupName,
                                                communicationType);

        member.setLeader(member);

        nameService.registerGroup(groupName, member);

        //Joining group
        String secondGroupMemberName = "user2";
        GroupMember member2 = new GroupMember(secondGroupMemberName,groupName,
                                                            communicationType);
        HashMap<String,Member> leaders = nameService.getGroups();
        Member leader = leaders.get(groupName);

        leader.joinGroup(member2);
//        assertEquals(status.toString(),Status.CONNECTED_TO_GROUP.toString());

        String[] memberNames = member2.getMemberNames();

        assertEquals(memberNames[0],groupLeader);
    }


//
    @Test
    public void reregisterGroup() {
    }


}