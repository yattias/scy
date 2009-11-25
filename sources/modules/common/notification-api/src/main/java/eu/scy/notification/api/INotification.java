package eu.scy.notification.api;

import java.util.Properties;

public interface INotification {

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
    public Properties getProperties();

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
