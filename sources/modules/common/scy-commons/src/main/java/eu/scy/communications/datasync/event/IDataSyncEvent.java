package eu.scy.communications.datasync.event;

import eu.scy.communications.message.ISyncMessage;


/**
 * Event object for datasync listeners 
 * 
 * @author thomasd
 */
public interface IDataSyncEvent {
    
    /**
     * gets the syncMessage
     * 
     * @return
     */
    public ISyncMessage getSyncMessage();
    
}