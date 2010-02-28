package eu.scy.presence;

/**
 * Event for when buddies are added and removed from a list
 * 
 * @author anthonyp
 *
 */
public interface IPresenceRosterEvent extends IPresenceEvent {
    
    public static String ADDED = "ADD";
    public static String DELETED = "DELETE";
    public static String UPDATED = "UPDATED";
    public static String MESSAGE_RECEIVED = "MESSAGE_RECEIVED";
    public static String MESSAGE_SENT = "MESSAGE_SENT";
    public static String PRESENCE_CHANGED = "PRESENCE_CHANGED";
}
