package eu.scy.communications.message.impl;


import org.xmpp.packet.Message;

import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.packet.extension.datasync.DataSyncPacketExtension;

/**
 * Sync message helper
 * 
 * @author anthonjp
 *
 */
public class SyncMessageHelper {

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
    public static ISyncMessage createSyncMessageWithDefaultExp(String toolSessionId, String toolId, String from, String content, String event, String persistenceId) {
        return createSyncMessage(toolSessionId, toolId, from, content, event, persistenceId, ISyncMessage.DEFAULT_MESSAGE_EXPIRATION_TIME);
    }

    /**
     *  Creates a SyncMessage from a org.xmpp.packet.message. This will be used when a sync message comes from the server. Convert xml in packet to POJO.
     *  
     *  @param syncMessage - the message to be converted
     *  @return ISyncMessage
     */
    public static ISyncMessage createSyncMessage(Message xmppMessage) {
    	DataSyncPacketExtension dspe = (DataSyncPacketExtension) xmppMessage.getExtension(DataSyncPacketExtension.ELEMENT_NAME, DataSyncPacketExtension.NAMESPACE);        
        if (dspe == null) {
            return null;
        }    	
        return dspe.toPojo();
    }
    

    
    /**
     * Create an org.xmpp.packet.message based on a SyncMessage. Message is ready to be sent to the data synchronizer.
     *  
     * @return org.xmpp.packet.Message
     */
    public static Message convertToXmppMessage(ISyncMessage syncMessage) {
        Message xmppMessage = new Message();        
        if (syncMessage.getFrom() == null) {
            return null;
        } else if (syncMessage.getFrom().contains("@")) {
            xmppMessage.setFrom(syncMessage.getFrom());            
        } else {
            xmppMessage.setFrom(syncMessage.getFrom() + "@" + ISyncMessage.XMPP_SERVER_ADDRESS);            
        }
        
        xmppMessage.setTo(ISyncMessage.DATA_SYNCHRONIZER_JID);
        DataSyncPacketExtension extension = new DataSyncPacketExtension(syncMessage);
        xmppMessage.addExtension(extension);
        return xmppMessage;
    }
    
    public static org.jivesoftware.smack.packet.Message convertToSmackXmppMessage(ISyncMessage syncMessage) {
        org.jivesoftware.smack.packet.Message xmppMessage = new org.jivesoftware.smack.packet.Message();        
        if (syncMessage.getFrom() == null) {
            return null;
        } else if (syncMessage.getFrom().contains("@")) {
            xmppMessage.setFrom(syncMessage.getFrom());            
        } else {
            xmppMessage.setFrom(syncMessage.getFrom() + "@" + ISyncMessage.XMPP_SERVER_ADDRESS);            
        }
        
        xmppMessage.setTo(ISyncMessage.DATA_SYNCHRONIZER_JID);
        DataSyncPacketExtension extension = new DataSyncPacketExtension(syncMessage);
        xmppMessage.addExtension(extension);
        return xmppMessage;
    }
    
}
