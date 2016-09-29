package gcom.status;

/**
 * Created by c12ton on 2016-09-29.
 */
public enum Status {
    CANT_CONNECT_TO_NAME_SERVICE(1,"Can't connect to name service"),
    CANT_CONNECT_TO_HOST(2,"Can't connect to name service"),
    CONNECTED_SUCCESS(3,"");

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
}
