package eu.scy.awareness.event;

import java.util.Collection;

/**
 * Event for when buddies are added and removed from a list
 * 
 * @author anthonyp
 *
 */
public interface IAwarenessRosterEvent extends IAwarenessEvent {
    
    public static String ADD = "ADD";
    public static String REMOVE = "REMOVE";
    public static String UPDATED = "UPDATED";
    
    /**
     * get the addresses of the rosterevent
     * 
     * @return
     */
    public Collection<String> getAddresses();
    
    /**
     * set the address of the rosterevent
     * 
     * @param addresses
     */
    public void setAddresses(Collection<String> addresses);
    
    /**
     * print out the user info
     * @return
     */
    public String toString();
    
    
    /**
	 * gets the room id
	 * 
	 * @return
	 */
    public String getRoomId();
	
	/**
	 * sets the room id
	 * 
	 */
	public void setRoomId(String roomId);
    
    
}
