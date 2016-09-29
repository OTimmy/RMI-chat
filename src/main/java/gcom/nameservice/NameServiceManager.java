package gcom.nameservice;

import gcom.groupmodule.Group;
import gcom.groupmodule.Member;
import gcom.status.Status;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by c12ton on 2016-09-29.
 */
public class NameServiceManager {

    private static Registry registry;
    private static String host;

    private static Status connectToRMI(String host){
        try {
            registry = LocateRegistry.getRegistry(host);
        } catch (RemoteException e) {
            e.printStackTrace();
            return Status.CANT_CONNECT_TO_HOST;
        }

        return null;
    }

    public static Group[] getGroups() {
            //RmiAddress = rmi/groups
//        registry.bind();
        return new gcom.groupmodule.Group[0];
    }

    public static Status registerGroup(String s, Member member) {


        return Status.CANT_CONNECT_TO_NAME_SERVICE;
    }

}
