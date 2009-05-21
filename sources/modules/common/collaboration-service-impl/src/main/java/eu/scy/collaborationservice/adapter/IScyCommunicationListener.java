package eu.scy.collaborationservice.adapter;

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
