package eu.scy.awareness.event;


/**
 * Presence event, handles change in presence and statuc 
 * 
 * @author anthonyp
 */
public interface IAwarePresenceEvent extends IAwarenessEvent {

    public String ONLINE = "ONLINE";
    public String OFFLINE = "OFFLINE";
    public String IDLE = "IDLE";
    
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
    
    
    /**
     * Gets the presence, ONLINE, OFFLINE, etcc
     * 
     * @return
     */
    public String getPresence();
    
    /**
     * Gets the status, i.g. im cooking
     * 
     * @return
     */
    public String getStatus();
    

}
