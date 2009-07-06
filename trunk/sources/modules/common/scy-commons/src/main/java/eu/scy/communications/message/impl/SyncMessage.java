package eu.scy.communications.message.impl;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.PacketExtension;

import eu.scy.communications.message.ISyncMessage;


/**
 * Message containing acutal data for synchronization (this is the new ScyMessage)
 * 
 * @author thomasd
 */
public class SyncMessage implements ISyncMessage {

    private static final Logger logger = Logger.getLogger(SyncMessage.class.getName());    

    //public static final String MESSAGE_TYPE_QUERY = "QUERY";
    //public static final String QUERY_TYPE_ALL = "ALL";
    
    private String toolSessionId;
    private String toolId;
    private String from; //we would benefit from using a JID-ish style on our user identities throughout scy
    private String to;
    private String content;
    private String event;
    private String persistenceId;
    private long expiration;


    /**
     * Default constructor
     */
    public SyncMessage() {
    }
    
    @Override
    public String toString() {
        StringBuffer output = new StringBuffer();
        output.append(" toolSessionId: " + toolSessionId + "\n");
        output.append(" toolId: " + toolId + "\n");
        output.append(" from: " + from + "\n");
        output.append(" to: " + to + "\n");
        output.append(" content: " + content + "\n");
        output.append(" event: " + event + "\n");
        output.append(" persistenceId: " + persistenceId + "\n");
        output.append(" expiration: " + String.valueOf(expiration));
        return output.toString();
    }
        
    
    @Override
    public String getToolSessionId() {
        return toolSessionId;
    }

    
    @Override
    public void setToolSessionId(String toolSessionId) {
        this.toolSessionId = toolSessionId;
    }

    
    @Override
    public String getToolId() {
        return toolId;
    }

    
    @Override
    public void setToolId(String toolId) {
        this.toolId = toolId;
    }

    
    @Override
    public String getFrom() {
        return from;
    }

    
    @Override
    public void setFrom(String from) {
        this.from = from;
    }

    
    @Override
    public String getContent() {
        return content;
    }

    
    @Override
    public void setContent(String content) {
        this.content = content;
    }

    
    @Override
    public String getEvent() {
        return event;
    }

    
    @Override
    public void setEvent(String event) {
        this.event = event;
    }

    
    @Override
    public long getExpiration() {
        return expiration;
    }

    
    @Override
    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }
        
    
    @Override
    public String getPersistenceId() {
        return persistenceId;
    }

    
    @Override
    public void setPersistenceId(String persistenceId) {
        this.persistenceId = persistenceId;
    }

    @Override
    public String getTo() {
        return this.to;
    }

    @Override
    public void setTo(String to) {
        this.to = to;
    }
}
