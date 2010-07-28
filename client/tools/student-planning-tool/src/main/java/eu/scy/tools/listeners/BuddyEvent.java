package eu.scy.tools.listeners;

import java.util.EventObject;

import eu.scy.awareness.IAwarenessUser;

public class BuddyEvent extends EventObject implements IBuddyEvent {

	public BuddyEvent(Object source) {
		super(source);
	}

	private static final long serialVersionUID = 1L;
	
	private IAwarenessUser user;
	private String type;
	
	@Override
	public IAwarenessUser getUser() {
		return user;
	}

	@Override
	public void setUser(IAwarenessUser user) {
		this.user = user;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

}
