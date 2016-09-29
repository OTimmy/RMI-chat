package gcom.groupmodule;

import gcom.status.Status;

import java.rmi.Remote;

/**
 * Created by c12ton on 2016-09-29.
 */
public interface GroupInterface extends Remote{
    public Status joinGroup(AbstractMember member);
    public Status setLeader(AbstractMember member);
    public AbstractMember getLeader();
}
