package eu.scy.datasync.api.event;

import eu.scy.datasync.api.ISyncMessage;


/**
 * Event object for datasync listeners 
 * 
 * @author thomasd
 */
public interface IDataSyncEvent {
    
    public abstract ISyncMessage getSyncMessage();
    
}