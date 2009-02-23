package eu.scy.awareness;

import java.util.EventObject;


public class AwarenessEvent extends EventObject {

    private String message;
    private String participant;

    public AwarenessEvent(Object source, String participant, String message){
        super(source);
        this.participant = participant;
        this.message = message;
    }

    public String getParticipant() {
        return participant;
    }
    
    public String getMessage(){
        return message;
    }
}
