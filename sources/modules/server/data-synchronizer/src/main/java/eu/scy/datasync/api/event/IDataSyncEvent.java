package eu.scy.datasync.api.event;

import eu.scy.communications.message.IScyMessage;


/**
 * Event object for collaboration service listeners 
 * 
 * @author anthonyp
 */
public interface IDataSyncEvent {
    
    //TODO: refactor 
    public abstract IScyMessage getScyMessage();
    
}