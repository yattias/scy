package eu.scy.awareness.event;

/**
 * Interface for general awareness events
 * 
 * @author anthonyp
 *
 */
public interface IAwarenessEvent {
    
    /**
     * Gets the user
     * @return
     */
   public String getUser();
   
   /**
    * Gets the message associated with this event
    * 
    * @return
    */
   public String getMessage();
    
}