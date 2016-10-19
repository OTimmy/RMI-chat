package gcom.status;

/**
 * Created by c12ton on 2016-10-04.
 */
public class GCOMException extends Exception{
    private GCOMError GCOMError;

    public GCOMException(String s) {
        super(s);
    }

    public GCOMException(GCOMError GCOMError) {
        super(GCOMError.toString());
    }

}
