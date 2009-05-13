package eu.scy.presence;

/**
 * Event for when buddies are added and removed from a list
 * 
 * @author anthonyp
 *
 */
public interface IPresenceListEvent extends IPresenceEvent {
    
    public static String ADD = "ADD";
    public static String REMOVE = "REMOVE";
}
