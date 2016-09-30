package gcom.groupmodule;

import gcom.nameservice.NameService;
import gcom.nameservice.NameServiceInterFace;
import gcom.status.Status;
import junit.framework.TestSuite;
import org.junit.*;
import org.junit.Before;
import org.junit.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by c12ton on 9/29/16.
 *  Test name service funcitonality with adding groups and members to them
 */
public class NameServiceManagerTest extends TestSuite {
    private static boolean setUpIsDone = false;
    private static NameServiceInterFace nameService;

    @Before
    public void setUp() throws Exception {
        if(!setUpIsDone) {
            NameService.main(null);
            try {
                nameService =  NameService.getNameService(null);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
        setUpIsDone = true;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testRegisterGroup() throws RemoteException {

        Member member = new Member("user1","Group 1");
        Status status = null;

        status = nameService.registerGroup("Group 1", member);


        if(status == Status.CANT_CONNECT_TO_NAME_SERVICE) {
            System.out.println("Error: " + status.toString());
            Assert.fail();
        }

        System.out.println("Status: " + status.toString());
        InterfaceMember[] leaders;

        leaders = nameService.getGroups();
        if(!leaders[0].getName().equals("user1")) {
            Assert.fail();
        }

    }

    @Test
    public void testJoinExistingGroup() {
    }

//
    @Test
    public void reregisterGroup() {
    }


}