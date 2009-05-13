package eu.scy.presence.event;

import eu.scy.presence.IPresenceListEvent;


public class PresenceListEvent extends PresenceEvent implements IPresenceListEvent {

    public PresenceListEvent(Object source, String user, String message) {
        super(source, user, message);
    }

}
