package eu.scy.notification;

import java.rmi.dgc.VMID;
import java.util.HashMap;
import java.util.Map;

import eu.scy.notification.api.INotification;

public class Notification implements INotification {

    public static final String PATH = "notification";

    private long timestamp;

    private Map<String, String> properties;

    private String toolId;
    
    private String userId;

    private String sender;

    private String uniqueId;

    private String mission;

    private String session;

    public Notification() {
        uniqueId = new VMID().toString();
        properties = new HashMap<String, String>();
    }

    /**
     * creates a new notification from XML
     * 
     * @param xml
     */
    public Notification(String uniqueId, String userId, String sender, String toolId, long timestamp, String mission, String session, Map<String, String> props) {
        this.uniqueId = uniqueId;
        this.userId = userId;
        this.sender = sender;
        this.toolId = toolId;
        this.timestamp = timestamp;
        this.mission = mission;
        this.session = session;
        properties = (props == null) ? new HashMap<String, String>() : props;
    }

    /**
     * returns the properties object
     */
    public Map<String, String> getProperties() {
        return properties;
    }

    /**
     * adds a key<>value pair
     * 
     * @param key
     * @param value
     */
    public void addProperty(String key, String value) {
        properties.put(key, value);

    }

    /**
     * @param key
     * @return value of 'key'
     */
    public String getProperty(String key) {
        return properties.get(key);
    }

    @Override
    public String getToolId() {
        return toolId;
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
    public void setToolId(String toolId) {
        this.toolId = toolId;
    }

    @Override
    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String getMission() {
        return mission;
    }

    @Override
    public String getUniqueID() {
        return uniqueId;
    }

    @Override
    public void setMission(String misson) {
        this.mission = misson;
    }

    @Override
    public void setUniqueID(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public String getSession() {
        return session;
    }

    @Override
    public void setSession(String sessionId) {
        this.session = sessionId;
    }

	@Override
	public String getUserId() {
		return userId;
	}

	@Override
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
