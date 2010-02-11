package eu.scy.notification.api;

import java.util.Map;

public interface INotification {

    /**
     * Sets the sessonId of this notification
     * 
     * @param sessionId
     *            The sessionId to set.
     */
    public void setSession(String sessionId);

    /**
     * Returns the sessionId of this notification
     * 
     * @return The sessionId of this notification
     */
    public String getSession();

    /**
     * Sets the Mission of this notification
     * 
     * @param misson
     *            The mission for this notification
     */
    public void setMission(String misson);

    /**
     * Returns the mission of this notification
     * 
     * @return The Mission as {@link String}
     */
    public String getMission();

    /**
     * Returns the UniqueID od this notification as a {@link String}
     * 
     * @return A {@link String} containing the UniqueID of this notification
     */
    public String getUniqueID();

    /**
     * Sets the UniqueID of this Notification
     * 
     * @param uniqueId
     *            The {@link String} containing the UniqueID
     */
    public void setUniqueID(String uniqueId);

    /**
     * Sets the receiving tool of this notification.
     * 
     * @param toolId
     *            either "scylab" or the id of a tool
     */
    public void setToolId(String toolI);

    /**
     * Returns the toolId of the receiving tool of this notification.
     * 
     * @return either "scylab" or the name of a tool
     */
    public String getToolId();

    /**
     * Sets the sender of this notification. * @param sender the xmpp name of the sender
     * 
     */
    public void setSender(String sender);

    /**
     * Sets the timestamp for this notification
     * 
     * @param timestamp
     *            the timestamp for this notification in millis from 1970
     */
    public void setTimestamp(long timestamp);

    /**
     * Returns the sender of this notification
     * 
     * @return the xmpp name of the sender
     */
    public String getSender();

    /**
     * Returns the timestamp of this notification
     * 
     * @return the timestamp as long (millis from 1970)
     */
    public long getTimestamp();

    /**
     * the information is stored in a Properties object
     */

    /**
     * returns the whole Properties object
     * 
     * @return whole Properties object
     */
    public Map<String, String[]> getProperties();

    /**
     * adds a new 'property'
     * 
     * @param key
     *            ->hashmap key
     * @param value
     *            -> hashmap value
     */
    public void addProperty(String key, String value);

    /**
     * returns the first value of the values array of 'key'
     * 
     * @param key
     * @return first value of 'key'
     */
    public String getFirstProperty(String key);

    /**
     * returns all values of 'key'
     * 
     * @param key
     * @return array containing all values of 'key'
     */
    public String[] getPropertyArray(String key);
    
    /**
     * Sets the userId, the actual XMPP receiver. Must be JID conform (Jabber ID).
     * 
     * @param userId 
     */
    public void setUserId(String userId);
    
    /**
     * Returns the user id, the actual XMPP receiver, of this notification as a JID conform String.
     *  
     * @return the userId as String
     */
    public String getUserId();

}
