package eu.scy.awareness.event;


/**
 * Fires when users presence and statuses changes
 * 
 * @author anthonyp
 *
 */
public interface IAwarenessPresenceListener  {
    
    /**
     * Handler for presence events
     * 
     * @param e
     */
    public void handleAwarenessPresenceEvent(IAwarePresenceEvent e);

}
