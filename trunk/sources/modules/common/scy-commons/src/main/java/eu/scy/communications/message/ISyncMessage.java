package eu.scy.communications.message;

public interface ISyncMessage {

	public static final String DATA_SYNC_XMPP_NAMESPACE = "eu:scy:datasync";
	// public static final String XMPP_SERVER_ADDRESS =
	// "wiki.intermedia.uio.no";
	public static final String XMPP_SERVER_ADDRESS = "imediamac09.uio.no";
	public static final String DATA_SYNCHRONIZER_JID = "scyhub."
			+ XMPP_SERVER_ADDRESS;
	public static final long DEFAULT_MESSAGE_EXPIRATION_TIME = 60 * 60 * 1000; // one
																				// hour

	/**
	 * Gets the tool session id
	 * 
	 * @return 
	 */
	public String getToolSessionId();

	/**
	 * sets the tool session id
	 * 
	 * @param toolSessionId
	 */
	public void setToolSessionId(String toolSessionId);

	/**
	 * get the tool id
	 * 
	 * @return
	 */
	public String getToolId();

	/**
	 * sets the tool id
	 * 
	 * @param toolId
	 */
	public void setToolId(String toolId);

	/**
	 * who is this message from
	 * 
	 * @return
	 */
	public String getFrom();

	/**
	 * sets who this is from
	 * 
	 * @param from
	 */
	public void setFrom(String from);

	/**
	 * gets the message content
	 * 
	 * @return
	 */
	public String getContent();

	/**
	 * set the content of the message
	 * 
	 * @param content
	 */
	public void setContent(String content);

	/**
	 * gets the event
	 * 
	 * @return
	 */
	public String getEvent();

	/**
	 * sets the event
	 * 
	 * @param event
	 */
	public void setEvent(String event);

	/**
	 * gets the expiration
	 * 
	 * @return
	 */
	public long getExpiration();

	/**
	 * sets the expiration time
	 * 
	 * @param defaultMessageExpirationTime
	 */
	public void setExpiration(long defaultMessageExpirationTime);

	/**
	 * gets the persistence id
	 * 
	 * @return
	 */
	public String getPersistenceId();

	/**
	 * sets the persistenceId
	 * 
	 * @param persistenceId
	 */
	public void setPersistenceId(String persistenceId);

}