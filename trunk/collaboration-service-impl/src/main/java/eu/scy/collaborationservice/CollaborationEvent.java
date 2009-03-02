package eu.scy.collaborationservice;

import java.util.EventObject;


public class CollaborationEvent extends EventObject implements ICollaborationEvent {

    private String message;
    private String participant;

    public CollaborationEvent(Object source, String participant, String message){
        super(source);
        this.participant = participant;
        this.message = message;
    }

    /* (non-Javadoc)
     * @see eu.scy.awareness.IAwarenessEvent#getParticipant()
     */
    public String getParticipant() {
        return participant;
    }
    
    /* (non-Javadoc)
     * @see eu.scy.awareness.IAwarenessEvent#getMessage()
     */
    public String getMessage(){
        return message;
    }
}
