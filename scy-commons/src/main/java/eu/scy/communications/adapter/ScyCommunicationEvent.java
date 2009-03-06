package eu.scy.communications.adapter;

import java.util.EventObject;


public class ScyCommunicationEvent extends EventObject {

    private String message;
    private String participant;

    public ScyCommunicationEvent(Object source, String participant, String message){
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
