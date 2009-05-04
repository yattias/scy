package eu.scy.awareness.presence;


/**
 * Fires when users presence and statuses changes
 * 
 * @author anthonyp
 *
 */
public interface IPresenceListener  {
    
    /**
     * Handler for presence events
     * 
     * @param e
     */
    public void handleAwarenessPresenceEvent(IPresenceStatusEvent e);

}
