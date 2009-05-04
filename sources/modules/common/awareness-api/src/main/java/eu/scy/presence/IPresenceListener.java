package eu.scy.presence;


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
    public void handlePresenceEvent(IPresenceStatusEvent e);

}
