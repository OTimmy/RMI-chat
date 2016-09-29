package gcom.groupmodule;

import gcom.nameservice.NameService;
import gcom.nameservice.NameServiceManager;
import gcom.status.Status;
import junit.framework.TestSuite;
import org.junit.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by c12ton on 9/29/16.
 */
public class NameServiceManagerTest extends TestSuite {
    @Before
    public void setUp() throws Exception {
//        NameService manager = new NameService();

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testRegisterGroup() {

        Member member = new Member("user1");

        Status status = NameServiceManager.registerGroup("Group 1",member);

        if(status == Status.CANT_CONNECT_TO_NAME_SERVICE) {
            System.out.println("Error: " + status.toString());
            Assert.fail();
        }

    }

    @Test
    public void testAddingExistingGroup() {
//        NameService.registerGroup("Group 1", member);
//        manager.registerGroup("Group 1", member);
    }

    @Test
    public void replacingLeader() {

    }

}