package gcom.nameservice;

import gcom.groupmodule.Group;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashSet;

/**
 * Created by c12ton on 9/29/16.
 */
public class NameService {
    public static final String GROUPS_REMOTE_STRING = "GROUPS";

    public static void main(String[] args) {
        startService();
    }

    private static void startService() {

        try {
            LocateRegistry.createRegistry(1099);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
