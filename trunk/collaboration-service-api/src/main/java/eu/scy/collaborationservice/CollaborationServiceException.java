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
        super(); // call superclass constructor
        badthing = "There was an error in the collaboration service";
    }
    
    public CollaborationServiceException(String err) {
        super(err); // call super class constructor
        badthing = err; // save message
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
