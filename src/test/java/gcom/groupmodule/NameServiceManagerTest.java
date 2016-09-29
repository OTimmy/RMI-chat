package gcom.groupmodule;

import junit.framework.TestSuite;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by c12ton on 9/29/16.
 */
public class NameServiceManagerTest extends TestSuite {
    private NameServiceManager manager;

    @Before
    public void setUp() throws Exception {
        manager = new NameServiceManager();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testAddGroups() {
        Member member = new Member();
        manager.registerGroup("Group 1",member);
        manager.registerGroup("Group 2", member);

        Group[] groups = manager.getGroups();

        Member leader1 = groups.getLeader();
    }

    @Test
    public void testAddingExistingGroup() {
        manager.registerGroup("Group 1", member);
        manager.registerGroup("Group 1", member);
    }

    @Test
    public void replacingLeader() {

    }

}