package eu.scy.communications.message;

import eu.scy.core.model.impl.ScyBaseObject;


public class ScyMessage extends ScyBaseObject {
    
    private String userName;
    private String toolName;
    private String objectType;
    private String to;
    private String from;
    private String messagePurpose;
    private long expiraton;
    

    public ScyMessage() {
    }
    
    
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

    
    public String getUserName() {
        return userName;
    }

    
    public void setUserName(String userName) {
        this.userName = userName;
    }

    
    public String getToolName() {
        return toolName;
    }

    
    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    
    public String getObjectType() {
        return objectType;
    }

    
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    
    public String getTo() {
        return to;
    }

    
    public void setTo(String to) {
        this.to = to;
    }

    
    public String getFrom() {
        return from;
    }

    
    public void setFrom(String from) {
        this.from = from;
    }

    
    public String getMessagePurpose() {
        return messagePurpose;
    }

    
    public void setMessagePurpose(String messagePurpose) {
        this.messagePurpose = messagePurpose;
    }
       
    public long getExpiraton() {
        return expiraton;
    }
    
    public void setExpiraton(long expiraton) {
        this.expiraton = expiraton;
    }

    
}
