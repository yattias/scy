package eu.scy.communications.message.impl;

import eu.scy.communications.message.IScyMessage;
import eu.scy.core.model.impl.ScyBaseObject;

/**
 * Represents an object that can be passed through the scy system
 * 
 * @author thomasd
 *
 */
public class ScyMessage extends ScyBaseObject implements IScyMessage {
    
    private String userName;
    private String toolName;
    private String objectType;
    private String to;
    private String from;
    private String messagePurpose;
    private long expiraton;
    private String session;
    
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
    public static IScyMessage createScyMessage(String username, String toolName, String id, String objectType, String name, String description, String to, String from, String messagePurpose, long expirationTime, String session) {
        IScyMessage sm = new ScyMessage();
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
        sm.setSession(session);
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
        output.append(" session: " + getSession() + "\n");
        return output.toString();
    }

    /* (non-Javadoc)
     * @see eu.scy.communications.message.IScyMessage#getUserName()
     */
    public String getUserName() {
        return userName;
    }

    /* (non-Javadoc)
     * @see eu.scy.communications.message.IScyMessage#setUserName(java.lang.String)
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /* (non-Javadoc)
     * @see eu.scy.communications.message.IScyMessage#getToolName()
     */
    public String getToolName() {
        return toolName;
    }

    /* (non-Javadoc)
     * @see eu.scy.communications.message.IScyMessage#setToolName(java.lang.String)
     */
    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    /* (non-Javadoc)
     * @see eu.scy.communications.message.IScyMessage#getObjectType()
     */
    public String getObjectType() {
        return objectType;
    }

    
    /* (non-Javadoc)
     * @see eu.scy.communications.message.IScyMessage#setObjectType(java.lang.String)
     */
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    /* (non-Javadoc)
     * @see eu.scy.communications.message.IScyMessage#getTo()
     */
    public String getTo() {
        return to;
    }

    /* (non-Javadoc)
     * @see eu.scy.communications.message.IScyMessage#setTo(java.lang.String)
     */
    public void setTo(String to) {
        this.to = to;
    }

    /* (non-Javadoc)
     * @see eu.scy.communications.message.IScyMessage#getFrom()
     */
    public String getFrom() {
        return from;
    }

    /* (non-Javadoc)
     * @see eu.scy.communications.message.IScyMessage#setFrom(java.lang.String)
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /* (non-Javadoc)
     * @see eu.scy.communications.message.IScyMessage#getMessagePurpose()
     */
    public String getMessagePurpose() {
        return messagePurpose;
    }

    /* (non-Javadoc)
     * @see eu.scy.communications.message.IScyMessage#setMessagePurpose(java.lang.String)
     */
    public void setMessagePurpose(String messagePurpose) {
        this.messagePurpose = messagePurpose;
    }
       
    /* (non-Javadoc)
     * @see eu.scy.communications.message.IScyMessage#getExpiraton()
     */
    public long getExpiraton() {
        return expiraton;
    }
    
    /* (non-Javadoc)
     * @see eu.scy.communications.message.IScyMessage#setExpiraton(long)
     */
    public void setExpiraton(long expiraton) {
        this.expiraton = expiraton;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getSession() {
        return session;
    }

    
}
