package gcom.status;

import java.util.HashMap;

/**
 * Created by c12ton on 2016-09-29.
 */
public enum Status {
    CANT_CONNECT_TO_NAME_SERVICE(1,"Can't connect to name service"),
    CANT_CONNECT_TO_HOST(2,"Can't connect to name service"),
    CREATED_GROUP_SUCCESS(3,"Group created!"),
    GROUP_ALREADY_EXISTS(4,"Group with that name already exists!"),
    CONNECTED_SUCCESS(5,"Yay, you are connected!");

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
