package eu.scy.presence;

import java.util.Collection;

import eu.scy.presence.IPresenceRosterEvent;


public class PresenceRosterEvent extends PresenceEvent implements IPresenceRosterEvent {

    public PresenceRosterEvent(Object source, Collection<String> users, String message, String eventType) {
        super(source, users, message, eventType);
    }

}
