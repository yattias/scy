package eu.scy.presence;

/**
 * Interface for general awareness events
 * 
 * @author anthonyp
 *
 */
public interface IPresenceEvent {
    
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