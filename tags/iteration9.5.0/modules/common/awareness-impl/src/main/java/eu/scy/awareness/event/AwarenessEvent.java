package eu.scy.awareness.event;

import java.util.EventObject;



public class AwarenessEvent extends EventObject implements IAwarenessEvent {

    private String message;
    private String user;

    public AwarenessEvent(Object source, String user, String message){
        super(source);
        this.user = user;
        this.message = message;
    }

    @Override
    public String getMessage() {
       return this.message;
    }

    @Override
    public String getUser() {
        return this.user;
    }

   
}
