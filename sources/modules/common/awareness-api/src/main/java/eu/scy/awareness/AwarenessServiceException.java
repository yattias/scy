package eu.scy.awareness;

/**
 * Exception handler for the awareness service
 * 
 * @author anthonyp
 *
 */
public class AwarenessServiceException extends Exception {
    
    private String badthing;
    
    public AwarenessServiceException() {
        super();
        badthing = "There was an error in the awareness service";
    }
    
    public AwarenessServiceException(String err) {
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
