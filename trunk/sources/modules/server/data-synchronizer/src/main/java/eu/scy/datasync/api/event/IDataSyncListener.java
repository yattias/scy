package eu.scy.datasync.api.event;


/**
 * Listener that does collaboration service events
 * 
 * @author anthonyp
 */
public interface IDataSyncListener  {
    
    /**
     * Handles collaboration events
     * @param e
     */
    public void handleCollaborationServiceEvent(IDataSyncEvent e);

}
