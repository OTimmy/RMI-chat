package rmi.debugservice;

import gcom.observer.Observer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by c12ton on 10/16/16.
 */
public class GroupObservers {
    private ConcurrentHashMap<String,Observer> userObservers;


    public GroupObservers() {
        userObservers = new ConcurrentHashMap<>();
    }

    public void addUser(String name,Observer b) {
        userObservers.put(name, b);
    }

    public Observer getObserver(String name) {
        return userObservers.get(name);
    }

    public String[] getNames() {
       return userObservers.keySet().toArray(new String []{});
    }

}
