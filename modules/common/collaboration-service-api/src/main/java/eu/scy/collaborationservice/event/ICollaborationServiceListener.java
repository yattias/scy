package eu.scy.collaborationservice.event;


/**
 * Listener that does collaboration service events
 * 
 * @author anthonyp
 */
public interface ICollaborationServiceListener  {
    
    /**
     * Handles collaboration events
     * @param e
     */
    public void handleCollaborationServiceEvent(ICollaborationServiceEvent e);

}
