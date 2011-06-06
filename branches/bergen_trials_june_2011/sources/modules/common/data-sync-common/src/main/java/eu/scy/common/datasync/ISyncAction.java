package eu.scy.common.datasync;

/**
 * The {@link ISyncAction} represents the user action for synchronization. It contains
 * all meta-data for identifying the action as well as the actual sync object to be
 * synchronized in collaborative sessions.
 * 
 * @author giemza
 */
public interface ISyncAction {
	
	/**
	 * The enum {@link Type} identifies the type of the action. An action can
	 * only be an add, change or remove action.
	 */
	public enum Type {
		add, change, remove
	}
	
	/**
	 * Sets the id of the Action. Id must be unique!
	 * 
	 * @param id the unique id of the action
	 */
	public void setId(String id);
	
	/**
	 * Returns the unique id of the action.
	 * 
	 * @return id the unique id of the actions as a {@link String}
	 */
	public String getId();
	
	/**
	 * Sets the {@link Type} of the action. {@link Type} can either be add, change or remove.
	 * 
	 * @param type the type of the action
	 */
	public void setType(Type type);
	
	/**
	 * Returns the {@link Type} of the action.
	 * 
	 * @return type the type of the action
	 */
	public Type getType();
	
	/**
	 * Sets the timestamp of the action as millis
	 * 
	 * @param timestamp the timestamp of the action
	 */
	public void setTimestamp(long timestamp);
	
	/**
	 * Returns the timestamp of the action.
	 * 
	 * @return timestamp the timestamp of the action
	 */
	public long getTimestamp();
	
	/**
	 * Sets the id of the user of the action. The id should be a valid {@link org.xmpp.packet.JID} {@link String}.
	 * 
	 * @param userId the user's id of the action
	 */
	public void setUserId(String userId);
	
	/**
	 * Returns the id of the user of the action.
	 * 
	 * @return userId the id of the user
	 */
	public String getUserId();
	
	/**
	 * Sets the id of the session the action has been created in.
	 * 
	 * @param sessionId the id of the session
	 */
	public void setSessionId(String sessionId);
	
	/**
	 * Returns the id of the session the actions has been created in.
	 * 
	 * @return sessionId the id of the session
	 */
	public String getSessionId();

	/**
	 * Sets the {@link ISyncObject} of the action. The object is the actual artifact
	 * that has been added, changed or deleted by this action.
	 * 
	 * @param syncObject the sync object of the action
	 */
	public void setSyncObject(ISyncObject syncObject);
	
	/**
	 * Returns the {@link ISyncObject} of the action.
	 * 
	 * @return syncObject the sync object of the action
	 */
	public ISyncObject getSyncObject();

}
