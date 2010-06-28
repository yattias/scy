package eu.scy.awareness.event;


/**
 * Fired when buddies are added or removed
 * 
 * @author anthonyp
 *
 */
public interface IAwarenessListListener  {
    
    /**
     * handles list events
     * 
     * @param e
     */
    public void handleAwarenessListEvent(IAwarenessListEvent e);

}
