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
     * Sets the receiver of this notification.
     * 
     * @param receiver
     *            either "scylab" or the name of a tool
     */
    public void setReceiver(String receiver);

    /**
     * Returns the receiver of this notification.
     * 
     * @return either "scylab" or the name of a tool
     */
    public String getReceiver();

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
    public Map<String, String> getProperties();

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
     * returns the value of 'key'
     * 
     * @param key
     * @return value of 'key'
     */
    public String getProperty(String key);

}
