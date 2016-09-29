package gcom.groupmodule;

import java.util.HashSet;

/**
 * Created by c12ton on 9/29/16.
 */
public class NameServiceManager {

    private HashSet<Group>groups;
    public NameServiceManager() {
        groups = new HashSet<Group>();
    }

    private void startService() {

    }

    public void registerGroup(String s, Member member) {
    }

    public Group[] getGroups() {
        return groups.toArray(new Group[]{});
    }
}
