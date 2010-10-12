package eu.scy.awareness.event;

/**
 * Event for when buddies are added and removed from a list
 * 
 * @author anthonyp
 *
 */
public interface IAwarenessListEvent extends IAwarenessEvent {
    
    public static String ADD = "ADD";
    public static String REMOVE = "REMOVE";
}
