package eu.scy.datasync.impl;

import org.apache.log4j.Logger;
import org.xmpp.packet.Message;
import org.xmpp.packet.PacketExtension;


/**
 * Message containing acutal data for synchronization (this is the new ScyMessage)
 * 
 * @author thomasd
 */
public class SyncMessage {

    public static final String DATA_SYNC_XMPP_NAMESPACE = "eu:scy:datasync";
    public static final String DATA_SYNCHRONIZER_JID = "datasynchronizer@wiki.intermedia.uio.no";

    private static final Logger logger = Logger.getLogger(SyncMessage.class.getName());    
    
    private String toolSessionId;
    private String toolId;
    private String from; //we would benefit from using a JID-ish style on our user identities throughout scy
    private String content;
    private String event;

    
    public SyncMessage() {
    }
    
    /**
     *  Creates a SyncMessage from a org.xmpp.packet.message. This will be used when a sync message comes from the server. Convert xml in packet to POJO.
     *  
     *  @param syncMessage - the message to be converted
     *  
     *  @return SyncMessage
     * 
     */
    public static SyncMessage createSyncMessage(Message xmppMessage) {
        SyncMessage syncMessage = new SyncMessage();
        syncMessage.setToolSessionId((xmppMessage.getChildElement("toolSessionId", DATA_SYNC_XMPP_NAMESPACE).getText()));
        syncMessage.setToolId(xmppMessage.getChildElement("toolId", DATA_SYNC_XMPP_NAMESPACE).getText());
        syncMessage.setContent(xmppMessage.getChildElement("content", DATA_SYNC_XMPP_NAMESPACE).getText());
        syncMessage.setEvent(xmppMessage.getChildElement("event", DATA_SYNC_XMPP_NAMESPACE).getText());
        syncMessage.setFrom(xmppMessage.getFrom().toString());        
        return syncMessage;
    }
    

    /**
     * Create an org.xmpp.packet.message based on a SyncMessage. Message is ready to be sent to the data synchronizer.
     *  
     * @return message 
     */
    public Message convertToXMPPMessage() {
        Message xmppMessage = new Message();        
        if (this.from == null) {
            logger.error("SyncMessage.from cannot be null");
            return null;
        }
        xmppMessage.setFrom(this.from);
        xmppMessage.setTo(DATA_SYNCHRONIZER_JID);
        
        PacketExtension extension;

        //toolSessionId
        extension = new PacketExtension("toolSessionId", DATA_SYNC_XMPP_NAMESPACE);
        extension.getElement().addText(this.toolSessionId);
        xmppMessage.addExtension(extension);        
        //toolID
        extension = new PacketExtension("toolId", DATA_SYNC_XMPP_NAMESPACE);
        extension.getElement().addText(this.toolId);
        xmppMessage.addExtension(extension);
        //content
        extension = new PacketExtension("content", DATA_SYNC_XMPP_NAMESPACE);
        extension.getElement().addCDATA(this.content);
        xmppMessage.addExtension(extension);        
        //event
        extension = new PacketExtension("event", DATA_SYNC_XMPP_NAMESPACE);
        extension.getElement().addText(this.event);
        xmppMessage.addExtension(extension);
        return xmppMessage;
    }
    

    public String getToolSessionId() {
        return toolSessionId;
    }
    
    public void setToolSessionId(String toolSessionId) {
        this.toolSessionId = toolSessionId;
    }
    
    public String getToolId() {
        return toolId;
    }
    
    public void setToolId(String toolId) {
        this.toolId = toolId;
    }
    
    public String getFrom() {
        return from;
    }
    
    public void setFrom(String from) {
        this.from = from;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getEvent() {
        return event;
    }
    
    public void setEvent(String event) {
        this.event = event;
    }
        
}
