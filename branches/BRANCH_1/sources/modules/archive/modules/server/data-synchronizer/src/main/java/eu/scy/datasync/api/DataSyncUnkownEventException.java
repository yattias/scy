package eu.scy.datasync.api;

/**
 * Exception handler for the data sync event
 * 
 * @author anthonyp
 *
 */
public class DataSyncUnkownEventException extends Exception {
    
    private String badthing;
    
    public DataSyncUnkownEventException() {
        super();
        badthing = "Unknown event type for data synchronization";
    }
    
    public DataSyncUnkownEventException(String err) {
        super(err);
        badthing = err;
    }

    /**
     * Return the error
     * 
     * @return
     */
    public String getError() {
        return badthing;
    }
    
}
