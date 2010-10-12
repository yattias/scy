package eu.scy.presence;

/**
 * Event for when packets are sent or received
 * 
 * @author thomasd
 *
 */
public interface IPresencePacketEvent extends IPresenceEvent {
    
    public static String ADDED = "ADD";
    public static String DELETED = "DELETE";
    public static String UPDATED = "UPDATED";
    public static String PRESENCE_CHANGED = "PRESENCE_CHANGED";
    public static String MESSAGE_RECEIVED = "MESSAGE_RECEIVED";
    public static String MESSAGE_SENT = "MESSAGE_SENT";
}
