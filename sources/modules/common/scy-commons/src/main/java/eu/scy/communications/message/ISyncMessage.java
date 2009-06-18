package eu.scy.communications.message;

public interface ISyncMessage {
    
    public abstract String getToolSessionId();
    
    public abstract void setToolSessionId(String toolSessionId);
    
    public abstract String getToolId();
    
    public abstract void setToolId(String toolId);
    
    public abstract String getFrom();
    
    public abstract void setFrom(String from);
    
    public abstract String getContent();
    
    public abstract void setContent(String content);
    
    public abstract String getEvent();
    
    public abstract void setEvent(String event);
    
    public abstract long getExpiration();

    public abstract void setExpiration(long defaultMessageExpirationTime);
    
    public abstract String getPersistenceId();

    public abstract void setPersistenceId(String persistenceId);
    
}