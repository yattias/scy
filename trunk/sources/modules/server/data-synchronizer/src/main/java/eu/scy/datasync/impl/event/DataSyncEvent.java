package eu.scy.datasync.impl.event;

import java.util.EventObject;

import eu.scy.datasync.api.ISyncMessage;
import eu.scy.datasync.api.event.IDataSyncEvent;

/**
 * Implementation of the DataSyncEvent interface
 *  
 * @author thomasd
 */
public class DataSyncEvent extends EventObject implements IDataSyncEvent {

    private static final long serialVersionUID = 1L;
    private ISyncMessage syncMessage;

    /**
     * constructor
     * 
     * @param source
     * @param participant
     * @param message
     */
    public DataSyncEvent(Object source, ISyncMessage syncMessage){
        super(source);
        this.syncMessage = syncMessage;
    }

    
    public ISyncMessage getSyncMessage(){
        return syncMessage;
    }
}
