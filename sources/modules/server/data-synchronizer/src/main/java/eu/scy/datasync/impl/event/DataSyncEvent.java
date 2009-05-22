package eu.scy.datasync.impl.event;

import java.util.EventObject;

import eu.scy.communications.message.IScyMessage;
import eu.scy.datasync.api.event.IDataSyncEvent;

/**
 * Implementation of the CollaborationServiceEvent inferace
 *  
 * @author anthonyp
 */
public class DataSyncEvent extends EventObject implements IDataSyncEvent {

    private static final long serialVersionUID = 1L;
    private IScyMessage scyMessage;

    /**
     * constructor
     * 
     * @param source
     * @param participant
     * @param message
     */
    public DataSyncEvent(Object source, IScyMessage scyMessage){
        super(source);
        this.scyMessage = scyMessage;
    }

    
    //TODO
    public IScyMessage getScyMessage(){
        return scyMessage;
    }
}
