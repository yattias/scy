package eu.scy.presence;


/**
 * Fired when buddies are added or removed
 * 
 * @author anthonyp
 *
 */
public interface IPresenceRosterListener  {
    
    /**
     * handles list events
     * 
     * @param e
     */
    public void handlePresenceRosterEvent(IPresenceRosterEvent e);

}
