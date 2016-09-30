package gcom.status;

import gcom.RemoteExtends;

import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by c12ton on 2016-09-29.
 */
public enum Status implements Remote {
    CANT_CONNECT_TO_NAME_SERVICE(1,"Can't connect to name service"),
    CANT_CONNECT_TO_HOST(2,"Can't connect to name service"),
    CREATED_GROUP_SUCCESS(3,""),
    GROUP_ALREADY_EXISTS(4,"Group with that name already exists!"),
    CONNECTED_SUCCESS(5,"");

    private final int code;
    private final String description;

    Status(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }

    public int getCode() {
        return code;
    }

}
