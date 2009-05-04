package eu.scy.awareness.presence;


/**
 * Fired when buddies are added or removed
 * 
 * @author anthonyp
 *
 */
public interface IPresenceListListener  {
    
    /**
     * handles list events
     * 
     * @param e
     */
    public void handleAwarenessListEvent(IPresenceListEvent e);

}
