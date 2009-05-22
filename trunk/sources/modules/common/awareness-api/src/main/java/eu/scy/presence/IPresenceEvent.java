package eu.scy.presence;

import java.util.Collection;

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
   public Collection<String> getUser();
   
   /**
    * Gets the message associated with this event
    * 
    * @return
    */
   public String getMessage();
   
   /**
    * Returns the type of event
    * 
    * @return
    */
   public String getEventType();
    
}