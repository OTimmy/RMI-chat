package gcom.status;

/**
 * Created by c12ton on 2016-09-29.
 */
public enum GCOMError {
    CANT_CONNECT_TO_NAME_SERVICE("Can't connect to name service"),
    CANT_CONNECT_TO_HOST("Can't connect to name service"),
    CREATED_GROUP_SUCCESS("Group created!"),
    GROUP_ALREADY_EXISTS("Group with that name already exists!"),
    NAME_EXISTS( "Name already exists!"),
    CANT_CONNECT_TO_GROUP("Can't connect the group");

    private final String description;

    GCOMError(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }

}
