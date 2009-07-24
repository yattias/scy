package eu.scy.awareness.event;

import eu.scy.awareness.AwarenessServiceException;


public class AwarenessPresenceEvent extends AwarenessEvent implements IAwarePresenceEvent {

    private String presence;
    private String status;
    
    public AwarenessPresenceEvent(Object source, String user, String message, String presence, String status) {
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
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("\n");
        sb.append("presence: ").append(presence);
        sb.append("status: ").append(status);
        return sb.toString();
    }

}
