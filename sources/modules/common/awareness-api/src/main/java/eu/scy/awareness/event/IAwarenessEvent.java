package eu.scy.awareness.event;

import eu.scy.awareness.IAwarenessUser;

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
   public IAwarenessUser getUser();
   
   /**
    * sets the user
    * 
    * @param user
    */
   public void setUser(IAwarenessUser user);
   
   /**
    * Gets the message associated with this event
    * 
    * @return
    */
   public String getMessage();
    
   /**
    * Sets the user
    * @return
    */
   public void setIAwarenessUser(IAwarenessUser user);
}