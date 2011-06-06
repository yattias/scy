package eu.scy.tools.listeners;


/**
 * Listens for new messages
 * 
 * @author anthonyp
 *
 */
public interface IPlanningToolBuddyListener {
    
	/**
	 * new buddy event
	 * 
	 * @param buddyEvent
	 */
    public void handleBuddyEvent(IBuddyEvent buddyEvent);

}
