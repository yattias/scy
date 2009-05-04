package eu.scy.presence;


/**
 * Presence event, handles change in presence and statuc 
 * 
 * @author anthonyp
 */
public interface IPresenceStatusEvent extends IPresenceEvent {

    public String ONLINE = "ONLINE";
    public String OFFLINE = "OFFLINE";
    public String IDLE = "IDLE";
   
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
