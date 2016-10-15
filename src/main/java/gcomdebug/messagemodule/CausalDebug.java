package gcomdebug.messagemodule;

import gcom.messagemodule.CausalOrdering;
import rmi.debugservice.DebugService;

/**
 * Created by c12ton on 10/15/16.
 */
public class CausalDebug extends CausalOrdering {

    private DebugService debugService;
    private String name;
    public CausalDebug(String name,String hostName) {
        super(name);
        this.name = name;
    }

    //updateVectorClock(String memberName)
        //increment vector clock
        //debugService.updateVectorClock(vector,this.name)
}
