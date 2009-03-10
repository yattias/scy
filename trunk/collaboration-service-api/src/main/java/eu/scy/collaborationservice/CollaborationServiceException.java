package eu.scy.collaborationservice;

/**
 * Exception handler for the collaboration service
 * 
 * @author anthonyp
 *
 */
public class CollaborationServiceException extends Exception {
    
    private String badthing;
    
    public CollaborationServiceException() {
        super();
        badthing = "There was an error in the collaboration service";
    }
    
    public CollaborationServiceException(String err) {
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
