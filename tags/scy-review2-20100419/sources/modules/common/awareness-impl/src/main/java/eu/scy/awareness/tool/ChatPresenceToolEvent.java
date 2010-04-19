package eu.scy.awareness.tool;

import java.util.ArrayList;
import java.util.List;

import eu.scy.awareness.IAwarenessUser;

public class ChatPresenceToolEvent implements IChatPresenceToolEvent {

	private String message = null;
	private List<IAwarenessUser> users  = new ArrayList<IAwarenessUser>();
	
	public ChatPresenceToolEvent(List<IAwarenessUser> users) {
		this.users = users;
	}
	
	public void setUsers(List<IAwarenessUser> users) {
		this.users = users;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public List<IAwarenessUser> getUsers() {
		return users;
	}

}
