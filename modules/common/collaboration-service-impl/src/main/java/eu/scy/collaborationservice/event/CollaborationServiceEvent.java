package eu.scy.collaborationservice.event;

import java.util.EventObject;

import eu.scy.communications.message.IScyMessage;

/**
 * Implementation of the CollaborationServiceEvent inferace
 *  
 * @author anthonyp
 */
public class CollaborationServiceEvent extends EventObject implements ICollaborationServiceEvent {

    private static final long serialVersionUID = 1L;
    private IScyMessage scyMessage;

    /**
     * constructor
     * 
     * @param source
     * @param participant
     * @param message
     */
    public CollaborationServiceEvent(Object source, IScyMessage scyMessage){
        super(source);
        this.scyMessage = scyMessage;
    }

    
    //TODO
    public IScyMessage getScyMessage(){
        return scyMessage;
    }
}
