package eu.scy.datasync.api.event;


/**
 * Listener that does datasync events
 * 
 * @author anthonyp
 */
public interface IDataSyncListener  {
    
    /**
     * Handles DataSync events
     * 
     * @param e
     */
    public void handleDataSyncEvent(IDataSyncEvent e);

}
