package eu.scy.presence.event;

import eu.scy.presence.IPresenceStatusEvent;


public class PresenceStatusEvent extends PresenceEvent implements IPresenceStatusEvent {

    private String presence;
    private String status;
    
    public PresenceStatusEvent(Object source, String user, String message, String presence, String status) {
        super(source, user, message);
        this.presence = presence;
        this.status = status;
    }

    @Override
    public String getPresence() {
        return presence;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

}
