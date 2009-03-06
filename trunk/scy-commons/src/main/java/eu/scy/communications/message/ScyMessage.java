package eu.scy.communications.message;

import eu.scy.core.model.impl.ScyBaseObject;


public class ScyMessage extends ScyBaseObject {
    
    private String userName;
    private String toolName;
    private String objectType;
    private String to;
    private String from;
    private String messagePurpose;
    

    public ScyMessage() {
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
   
    
}
