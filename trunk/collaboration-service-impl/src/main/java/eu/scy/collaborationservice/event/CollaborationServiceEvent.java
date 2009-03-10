package eu.scy.collaborationservice.event;

import java.util.EventObject;

/**
 * Implementation of the CollaborationServiceEvent inferace
 *  
 * @author anthonyp
 */
public class CollaborationServiceEvent extends EventObject implements ICollaborationServiceEvent {

    private static final long serialVersionUID = 1L;
    private String message;
    private String participant;

    /**
     * constructor
     * 
     * @param source
     * @param participant
     * @param message
     */
    public CollaborationServiceEvent(Object source, String participant, String message){
        super(source);
        this.participant = participant;
        this.message = message;
    }

    //TODO
    public String getParticipant() {
        return participant;
    }
    
    //TODO
    public String getMessage(){
        return message;
    }
}
