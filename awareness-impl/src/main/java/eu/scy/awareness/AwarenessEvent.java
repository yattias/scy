package eu.scy.awareness;

import java.util.EventObject;

import eu.scy.awareness.api.IAwarenessEvent;


public class AwarenessEvent extends EventObject implements IAwarenessEvent {

    private String message;
    private String participant;

    public AwarenessEvent(Object source, String participant, String message){
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
