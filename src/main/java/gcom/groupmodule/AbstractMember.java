package gcom.groupmodule;

import java.util.HashSet;

/**
 * Created by c12ton on 9/29/16.
 */
public abstract class AbstractMember {
    private String member_id;
    private HashSet<AbstractMember> members;
//    private
    public  abstract void init();
}
