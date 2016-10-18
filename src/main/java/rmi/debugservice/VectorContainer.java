package rmi.debugservice;

import java.util.HashMap;

/**
 * Created by c12ton on 10/18/16.
 */
public interface VectorContainer {

    String getGroupName();
    String getName();

    HashMap<String,Integer> getVectorClock();

}
