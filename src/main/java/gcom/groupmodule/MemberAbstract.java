package gcom.groupmodule;

import java.util.HashSet;

/**
 * Created by c12ton on 9/29/16.
 */
public abstract class MemberAbstract {
    private String member_id;
    private HashSet<MemberAbstract> members;
//    private
    public  abstract void init();
}
