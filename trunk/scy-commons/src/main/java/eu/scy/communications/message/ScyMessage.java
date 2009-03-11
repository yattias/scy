package eu.scy.communications.message;

import eu.scy.core.model.impl.ScyBaseObject;

/**
 * Represents an object that can be passed through the scy system
 * 
 * @author thomasd
 *
 */
public class ScyMessage extends ScyBaseObject {
    
    private String userName;
    private String toolName;
    private String objectType;
    private String to;
    private String from;
    private String messagePurpose;
    private long expiraton;
    
    public static final String MESSAGE_TYPE_QUERY = "QUERY";
    public static final String QUERY_TYPE_ALL = "ALL";
    
    
    public ScyMessage() {
    }
    
    /**
     * creator for a new message
     * 
     * @param username
     * @param toolName
     * @param id
     * @param objectType
     * @param name
     * @param description
     * @param to
     * @param from
     * @param messagePurpose
     * @param expirationTime
     * @return
     */
    public static ScyMessage createScyMessage(String username, String toolName, String id, String objectType, String name, String description, String to, String from, String messagePurpose, long expirationTime) {
        ScyMessage sm = new ScyMessage();
        sm.setUserName(username);
        sm.setToolName(toolName);
        sm.setId(id);
        sm.setObjectType(objectType);
        sm.setName(name);
        sm.setDescription(description);
        sm.setTo(to);
        sm.setFrom(from);
        sm.setMessagePurpose(messagePurpose);
        sm.setExpiraton(expirationTime);
        return sm;
    }
    
    
    @Override
    public String toString() {
        StringBuffer output = new StringBuffer();
        output.append(" userName: " + userName + "\n");
        output.append(" toolName: " + toolName + "\n");
        output.append(" objectType: " + objectType + "\n");
        output.append(" messagePurpose: " + messagePurpose + "\n");
        output.append(" from: " + from + "\n");
        output.append(" to: " + to + "\n");
        output.append(" expiration: " + expiraton + "\n");
        output.append(" name: " + super.getName() + "\n");
        output.append(" id: " + super.getId() + "\n");
        output.append(" description: " + super.getDescription() + "\n");
        return output.toString();
    }

    /**
     * Returns the user name
     * 
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * sets the user name
     * 
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the tool name
     * 
     * @return
     */
    public String getToolName() {
        return toolName;
    }

    /**
     * Sets the tool name
     * 
     * @param toolName
     */
    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    /**
     * Gets the type of object this message represents
     * 
     * @return
     */
    public String getObjectType() {
        return objectType;
    }

    
    /**
     * Set the object type
     * 
     * @param objectType
     */
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    /**
     * Get the to
     * 
     * @return
     */
    public String getTo() {
        return to;
    }

    /**
     * set the to
     * 
     * @param to
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Get the from
     * 
     * @return
     */
    public String getFrom() {
        return from;
    }

    /**
     * sets the from
     * 
     * @param from
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Get the message purpose
     * 
     * @return
     */
    public String getMessagePurpose() {
        return messagePurpose;
    }

    /**
     * set message purpose
     * 
     * @param messagePurpose
     */
    public void setMessagePurpose(String messagePurpose) {
        this.messagePurpose = messagePurpose;
    }
       
    /**
     * Get the expiration
     * 
     * @return
     */
    public long getExpiraton() {
        return expiraton;
    }
    
    /**
     * Sets the expiration of this message
     * 
     * @param expiraton
     */
    public void setExpiraton(long expiraton) {
        this.expiraton = expiraton;
    }

    
}
