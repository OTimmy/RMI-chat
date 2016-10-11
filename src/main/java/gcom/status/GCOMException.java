package gcom.status;

/**
 * Created by c12ton on 2016-10-04.
 */
public class GCOMException extends Exception{
    private Status status;

    public GCOMException(String s) {
        super(s);
    }

    public GCOMException(Status status) {
        super(status.toString());
    }

    public int getCode() {
        return status.getCode();
    }
}
