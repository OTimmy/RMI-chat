package gcom.status;

/**
 * Created by c12ton on 2016-09-29.
 */
public enum GCOMError {
    CANT_CONNECT_TO_NAME_SERVICE(1,"Can't connect to name service"),
    CANT_CONNECT_TO_HOST(2,"Can't connect to name service"),
    CREATED_GROUP_SUCCESS(3,"Group created!"),
    GROUP_ALREADY_EXISTS(4,"Group with that name already exists!"),
    CONNECTED_SUCCESS(5,"Yay, you are connected!"),
    CONNECTED_TO_GROUP(6,"Yay u connected to group"),
    NAME_EXISTS(7, "Name already exists!");

    private final int code;
    private final String description;

    GCOMError(int code, String description) {
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
