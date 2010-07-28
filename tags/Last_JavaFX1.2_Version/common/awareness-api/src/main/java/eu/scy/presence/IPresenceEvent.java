package eu.scy.presence;

import java.util.Collection;

/**
 * Interface for general awareness events
 * 
 * @author anthonyp
 *
 */
public interface IPresenceEvent {
    
	public static String AVAILABLE = "available";
	public static String UNAVAILABLE = "unavailable";
	public static String WAITING = "waiting";
	
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