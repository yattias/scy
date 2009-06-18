package eu.scy.datasync.adapter;

import java.util.EventObject;

import eu.scy.communications.message.ISyncMessage;

/**
 * Represents an event in the scycommunication layer
 * 
 * @author thomasd
 */
public class ScyCommunicationEvent extends EventObject {

    private ISyncMessage syncMessage;
   
    /**
     * Contructor
     * 
     * @param source
     * @param syncMessage
     */
    public ScyCommunicationEvent(Object source, ISyncMessage syncMessage){
        super(source);
        this.syncMessage = syncMessage;
    }

    
    /**
     * Get the syncMessage
     * 
     * @return syncMessage
     */
    public ISyncMessage getSyncMessage() {
        return syncMessage;
    }
    
}
