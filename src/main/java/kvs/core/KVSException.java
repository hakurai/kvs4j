package kvs.core;

public class KVSException extends Exception {
    private static final long serialVersionUID = 1L;

    public KVSException() {
        super();
    }

    public KVSException( String message ) {
        super( message );
    }

    public KVSException( Throwable cause ){
        super( cause );
    }
}
