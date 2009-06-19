package eu.scy.communications.message.impl;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.PacketExtension;

import eu.scy.communications.message.ISyncMessage;


/**
 * used by the xmpp message xmlify data sync.
 * 
 * @author thomasd
 *
 */
public class DataSyncPacketExtension extends DefaultPacketExtension implements PacketExtension {
    
    private static final Logger logger = Logger.getLogger(DataSyncPacketExtension.class.getName());
        
    public static final String ELEMENT_NAME = "syncData";
    public static final String NAMESPACE = "eu:scy:datasync";
    

    public DataSyncPacketExtension() {
        super(ELEMENT_NAME, NAMESPACE);
    }
    
    
    public DataSyncPacketExtension(ISyncMessage syncMessage) {
        super(ELEMENT_NAME, NAMESPACE);
        setValue("toolSessionId", syncMessage.getToolSessionId());
        setValue("toolId", syncMessage.getToolId());
        setValue("from", syncMessage.getFrom());
        setValue("content", syncMessage.getContent());
        setValue("event", syncMessage.getEvent());
        setValue("persistenceId", syncMessage.getPersistenceId());
        setValue("expiration", String.valueOf(syncMessage.getExpiration()));
    }
    
    
    public ISyncMessage toPojo() {
        return SyncMessage.createSyncMessage(getValue("toolSessionId"), 
                                            getValue("toolId"), 
                                            getValue("from"), 
                                            getValue("content"), 
                                            getValue("event"), 
                                            getValue("persistenceId"), 
                                            Long.parseLong(getValue("expiration")));
    }
    
    
    public String toXML() {
        StringBuffer buf = new StringBuffer();        
        buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append(">");
        buf.append("<toolSessionId>").append(getValue("toolSessionId")).append("</toolSessionId>");
        buf.append("<toolId>").append(getValue("toolId")).append("</toolId>");
        buf.append("<from>").append(getValue("from")).append("</from>");
        buf.append("<content><![CDATA[").append(getValue("content")).append("]]></content>");
        buf.append("<event>").append(getValue("event")).append("</event>");
        buf.append("<persistenceId>").append(getValue("persistenceId")).append("</persistenceId>");
        buf.append("<expiration>").append(getValue("expiration")).append("</expiration>");
        buf.append("</").append(getElementName()).append(">");        
        return buf.toString();
    }
    
    
    @Override
    public String getValue(String value) {
        if (super.getValue(value) == null) {
            return "";
        }
        return super.getValue(value);
    }
    
}

