package eu.scy.presence;

/**
 * Exception handler for the awareness service
 * 
 * @author anthonyp
 *
 */
public class PresenceModuleException extends Exception {
    
    private String badthing;
    
    public PresenceModuleException() {
        super();
        badthing = "There was an error in the awareness service";
    }
    
    public PresenceModuleException(String err) {
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
