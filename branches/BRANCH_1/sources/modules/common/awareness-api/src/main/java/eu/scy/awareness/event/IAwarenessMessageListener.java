package eu.scy.awareness.event;


/**
 * Listens for new messages
 * 
 * @author anthonyp
 *
 */
public interface IAwarenessMessageListener {
    
    /**
     * Handles a message event
     * 
     * @param awarenessEvent
     */
    public void handleAwarenessMessageEvent(IAwarenessEvent awarenessEvent);

}
