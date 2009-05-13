package eu.scy.presence.event;

import java.util.EventObject;

import eu.scy.presence.IPresenceEvent;



public class PresenceEvent extends EventObject implements IPresenceEvent {

    private String message;
    private String user;

    public PresenceEvent(Object source, String user, String message){
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
