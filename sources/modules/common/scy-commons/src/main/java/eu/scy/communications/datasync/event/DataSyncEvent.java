package eu.scy.communications.datasync.event;

import java.util.EventObject;

import eu.scy.communications.message.ISyncMessage;

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

    /**
     * gets the syncMessage
     * 
     * @return
     */
    public ISyncMessage getSyncMessage(){
        return syncMessage;
    }
}
