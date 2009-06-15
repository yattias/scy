package eu.scy.datasync.impl;

import org.apache.log4j.Logger;
import org.xmpp.packet.Message;
import org.xmpp.packet.PacketExtension;

import eu.scy.datasync.api.ISyncMessage;


/**
 * Message containing acutal data for synchronization (this is the new ScyMessage)
 * 
 * @author thomasd
 */
public class SyncMessage implements ISyncMessage {

    private static final Logger logger = Logger.getLogger(SyncMessage.class.getName());    

    public static final String DATA_SYNC_XMPP_NAMESPACE = "eu:scy:datasync";
    public static final String XMPP_SERVER_ADDRESS = "wiki.intermedia.uio.no";
    public static final String DATA_SYNCHRONIZER_JID = "datasynchronizer@" + XMPP_SERVER_ADDRESS;
    public static final long DEFAULT_MESSAGE_EXPIRATION_TIME = 60*60*1000; // one hour
    
    public static final String MESSAGE_TYPE_QUERY = "QUERY";
    //public static final String QUERY_TYPE_ALL = "ALL";
    
    private String toolSessionId;
    private String toolId;
    private String from; //we would benefit from using a JID-ish style on our user identities throughout scy
    private String content;
    private String event;
    private String persistenceId;
    private long expiration;

    
    public SyncMessage() {
    }
    
    /**
     * Wrapper without expiration time
     * 
     * @param toolSessionId
     * @param toolId
     * @param from
     * @param content
     * @param event
     * @param persistenceId
     * @return
     */
    public static ISyncMessage createSyncMessage(String toolSessionId, String toolId, String from, String content, String event, String persistenceId) {
        return createSyncMessage(toolSessionId, toolId, from, content, event, persistenceId, DEFAULT_MESSAGE_EXPIRATION_TIME);
    }
    
    
    /**
     *  Creates a SyncMessage from a bunch of strings. Typically when coming from a tool.
     *  
     * @param toolSessionId
     * @param toolId
     * @param from
     * @param content
     * @param event
     * @param persistenceId
     * @param expiration
     * @return ISyncMessage
     */
    public static ISyncMessage createSyncMessage(String toolSessionId, String toolId, String from, String content, String event, String persistenceId, long expiration) {
        ISyncMessage syncMessage = new SyncMessage();
        syncMessage.setToolSessionId(toolSessionId);
        syncMessage.setToolId(toolId);
        syncMessage.setFrom(from);
        syncMessage.setContent(content);
        syncMessage.setEvent(event);
        syncMessage.setPersistenceId(persistenceId);
        syncMessage.setExpiration(expiration);
        return syncMessage;        
    }
    
    
    /**
     *  Creates a SyncMessage from a org.xmpp.packet.message. This will be used when a sync message comes from the server. Convert xml in packet to POJO.
     *  
     *  @param syncMessage - the message to be converted
     *  @return ISyncMessage
     */
    public static ISyncMessage createSyncMessage(Message xmppMessage) {
        return createSyncMessage(
                xmppMessage.getChildElement("toolSessionId", DATA_SYNC_XMPP_NAMESPACE).getText(), 
                xmppMessage.getChildElement("toolId", DATA_SYNC_XMPP_NAMESPACE).getText(), 
                xmppMessage.getFrom().toString(),
                xmppMessage.getChildElement("content", DATA_SYNC_XMPP_NAMESPACE).getText(), 
                xmppMessage.getChildElement("event", DATA_SYNC_XMPP_NAMESPACE).getText(),
                xmppMessage.getChildElement("persistenceId", DATA_SYNC_XMPP_NAMESPACE).getText());
    }
    

    /**
     * Create an org.xmpp.packet.message based on a SyncMessage. Message is ready to be sent to the data synchronizer.
     *  
     * @return message 
     */
    public Message convertToXMPPMessage() {
        Message xmppMessage = new Message();        
        if (from == null) {
            logger.error("From cannot be null");
            return null;
        } else if (from.contains("@")) {
            xmppMessage.setFrom(from);            
        } else {
            xmppMessage.setFrom(from + "@" + XMPP_SERVER_ADDRESS);            
        }
        
        xmppMessage.setTo(DATA_SYNCHRONIZER_JID);
        
        PacketExtension extension;

        //toolSessionId
        extension = new PacketExtension("toolSessionId", DATA_SYNC_XMPP_NAMESPACE);
        extension.getElement().addText(toolSessionId);
        xmppMessage.addExtension(extension);        
        //toolID
        extension = new PacketExtension("toolId", DATA_SYNC_XMPP_NAMESPACE);
        extension.getElement().addText(toolId);
        xmppMessage.addExtension(extension);
        //from
        extension = new PacketExtension("from", DATA_SYNC_XMPP_NAMESPACE);
        extension.getElement().addText(from);
        xmppMessage.addExtension(extension);
        //content
        extension = new PacketExtension("content", DATA_SYNC_XMPP_NAMESPACE);
        extension.getElement().addCDATA(content);
        xmppMessage.addExtension(extension);        
        //event
        extension = new PacketExtension("event", DATA_SYNC_XMPP_NAMESPACE);
        extension.getElement().addText(event);
        xmppMessage.addExtension(extension);
        //persistenceId
        extension = new PacketExtension("persistenceId", DATA_SYNC_XMPP_NAMESPACE);
        extension.getElement().addText(String.valueOf(persistenceId));
        xmppMessage.addExtension(extension);
        
        return xmppMessage;
    }
    
    
    @Override
    public String toString() {
        StringBuffer output = new StringBuffer();
        output.append(" toolSessionId: " + toolSessionId + "\n");
        output.append(" toolId: " + toolId + "\n");
        output.append(" from: " + from + "\n");
        output.append(" content: " + content + "\n");
        output.append(" event: " + event + "\n");
        output.append(" persistenceId: " + persistenceId + "\n");
        output.append(" expiration: " + String.valueOf(expiration) + "\n");
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
}
