package eu.scy.datasync.adapter;

/**
 * Interface for handling scycommunication events
 * 
 * @author anthonyp
 */
public interface IScyCommunicationListener  {
   
    /**
     * action handler for this event
     * 
     * @param scyCommunicationEvent
     */
    public void handleCommunicationEvent(ScyCommunicationEvent scyCommunicationEvent);

}
