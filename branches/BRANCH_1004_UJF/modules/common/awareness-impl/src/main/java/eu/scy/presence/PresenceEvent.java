package eu.scy.presence;

import java.util.Collection;
import java.util.EventObject;

import eu.scy.presence.IPresenceEvent;

public class PresenceEvent extends EventObject implements IPresenceEvent {

	private static final long serialVersionUID = 1L;
	private String message;
    private Collection<String> users;
	private String eventType;

    public PresenceEvent(Object source, Collection<String> users, String message, String eventType){
        super(source);
        this.users = users;
        this.message = message;
        this.eventType = eventType;
    }

    @Override
    public String getMessage() {
       return this.message;
    }

	@Override
	public Collection<String> getUser() {
		return this.users;
	}

	@Override
	public String getEventType() {
		return this.eventType;
	}

   
}
