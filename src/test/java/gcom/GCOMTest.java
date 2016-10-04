package gcom;

import gcom.groupmodule.GroupMember;
import gcom.nameservice.NameService;
import gcom.nameservice.NameServiceInterFace;
import gcom.status.GCOMException;
import gcom.status.Status;
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

    //private userQue
    @Before
    public void setUp() throws Exception {
        nameService = new NameService();
        try {

            if(!setUpIsDone) {
                registry = LocateRegistry.createRegistry(1099);
                setUpFakeGroup();
            }

            registry.rebind(NameService.class.getSimpleName(), nameService);
            setUpIsDone = true;
            System.out.println("Sever is ready!");
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private void setUpFakeGroup() throws RemoteException {
        GroupMember member = new GroupMember(GROUP_USER_NAME,GROUP_NAME,"typcom");
        nameService.registerGroup(GROUP_NAME,member);
    }



    @Test
    public void testJoinGroup() throws RemoteException, NotBoundException, GCOMException {

        String username = "BigRed";
        GCOM gcom = new GCOM(null);

        String[] membersName =  gcom.connectToGroup(GROUP_NAME,username);

        assertArrayEquals(new String[]{GROUP_USER_NAME,username},membersName);
    }

}