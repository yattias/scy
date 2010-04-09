package eu.scy.presence;

import java.util.Collection;

/**
 * Event for when packets are sent or received
 * 
 * @author thomasd
 *
 */
public class PresencePacketEvent extends PresenceEvent implements IPresencePacketEvent {

    public PresencePacketEvent(Object source, Collection<String> users, String message, String eventType) {
        super(source, users, message, eventType);
    }

}
