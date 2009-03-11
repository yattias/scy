package eu.scy.collaborationservice.event;

import java.util.EventObject;

import eu.scy.communications.message.ScyMessage;

/**
 * Implementation of the CollaborationServiceEvent inferace
 *  
 * @author anthonyp
 */
public class CollaborationServiceEvent extends EventObject implements ICollaborationServiceEvent {

    private static final long serialVersionUID = 1L;
    private ScyMessage scyMessage;

    /**
     * constructor
     * 
     * @param source
     * @param participant
     * @param message
     */
    public CollaborationServiceEvent(Object source, ScyMessage scyMessage){
        super(source);
    }

    
    //TODO
    public ScyMessage getScyMessage(){
        return scyMessage;
    }
}
