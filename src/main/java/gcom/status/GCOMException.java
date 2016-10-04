package gcom.status;

/**
 * Created by c12ton on 2016-10-04.
 */
public class GCOMException extends Exception{
    public GCOMException(Status status) {
        super(status.toString());
    }
}
