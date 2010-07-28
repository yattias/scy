package eu.scy.awareness.tool;

import java.util.List;

import eu.scy.awareness.IAwarenessUser;

public interface IChatPresenceToolEvent {

	/**
	 * users involed in this event
	 * @return
	 */
	 public List<IAwarenessUser> getUsers();
	 
	 /**
	  * message string dispatched
	  * @return
	  */
	 
	public String getMessage();

	public void setMessage(String message);

	public void setUsers(List<IAwarenessUser> users);
}
