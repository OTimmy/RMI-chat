package rmi.debugservice;

import java.util.HashMap;

/**
 * Created by c12ton on 10/18/16.
 */
public class VectorData implements VectorContainer {

    private String groupName;
    private String name;
    private HashMap<String,Integer> vector;

    public VectorData(String groupName, String name, HashMap<String,Integer> vector) {
        this.groupName = groupName;
        this.name = name;
        this.vector = vector;

    }

    @Override
    public String getGroupName() {
        return groupName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public HashMap<String, Integer> getVectorClock() {
        return vector;
    }
}
