package eu.scy.datasync.api;

/**
 * Exception handler for the collaboration service
 * 
 * @author anthonyp
 *
 */
public class DataSyncException extends Exception {
    
    private String badthing;
    
    public DataSyncException() {
        super();
        badthing = "There was an error in the collaboration service";
    }
    
    public DataSyncException(String err) {
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
