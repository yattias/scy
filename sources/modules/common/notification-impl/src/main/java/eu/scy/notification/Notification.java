package eu.scy.notification;

import java.util.Properties;

import eu.scy.notification.api.INotification;

public class Notification implements INotification {

    private long timestamp;

    private Properties properties;

    private String receiver;

    private String sender;

    public Notification() {
        properties = new Properties();
    }

    /**
     * creates a new notification from XML
     * 
     * @param xml
     */
    public Notification(String sender, String receiver, long timestamp, Properties props) {
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
        properties = (props == null) ? new Properties() : props;
    }

    /**
     * returns the properties object
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * adds a key<>value pair
     * 
     * @param key
     * @param value
     */
    public void addProperty(String key, String value) {
        properties.setProperty(key, value);

    }

    /**
     * @param key
     * @return value of 'key'
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    @Override
    public String getReceiver() {
        return receiver;
    }

    @Override
    public String getSender() {
        return sender;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Override
    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
