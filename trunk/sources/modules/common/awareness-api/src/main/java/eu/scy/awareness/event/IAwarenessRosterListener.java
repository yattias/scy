package eu.scy.awareness.event;


/**
 * Fired when buddies are added or removed
 * 
 * @author anthonyp
 *
 */
public interface IAwarenessRosterListener  {
    
    /**
     * handles list events
     * 
     * @param e
     */
    public void handleAwarenessRosterEvent(IAwarenessRosterEvent e);

}
