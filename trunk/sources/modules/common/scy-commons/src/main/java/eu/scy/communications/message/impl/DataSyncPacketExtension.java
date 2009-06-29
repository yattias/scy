package eu.scy.communications.message.impl;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.xmpp.packet.PacketExtension;

import eu.scy.communications.message.ISyncMessage;


/**
 * used by the xmpp message xmlify data sync.
 * 
 * @author thomasd
 *
 */
public class DataSyncPacketExtension extends DefaultPacketExtension {
    
    private static final Logger logger = Logger.getLogger(DataSyncPacketExtension.class.getName());
        
    public static final String ELEMENT_NAME = "syncData";
    public static final String NAMESPACE = "eu:scy:datasync";
    
    public static final String TOOL_SESSION_ID = "toolSessionId";
    public static final String TOOL_ID = "toolId";
    public static final String FROM = "from";
    public static final String CONTENT = "content";
    public static final String EVENT = "event";
    public static final String PERSISTENCE_ID = "persistenceId";
    public static final String EXPIRATION = "expiration";
    

    public DataSyncPacketExtension() {
        super(ELEMENT_NAME, NAMESPACE);
    }
    
    
    public DataSyncPacketExtension(ISyncMessage syncMessage) {
        super(ELEMENT_NAME, NAMESPACE);
        setValue(TOOL_SESSION_ID, syncMessage.getToolSessionId());
        setValue(TOOL_ID, syncMessage.getToolId());
        setValue(FROM, syncMessage.getFrom());
        setValue(CONTENT, syncMessage.getContent());
        setValue(EVENT, syncMessage.getEvent());
        setValue(PERSISTENCE_ID, syncMessage.getPersistenceId());
        setValue(EXPIRATION, String.valueOf(syncMessage.getExpiration()));
    }
    
    
    public ISyncMessage toPojo() {
        logger.debug("expiration >" + getValue(EXPIRATION) + "<");
        return SyncMessage.createSyncMessage(getValue(TOOL_SESSION_ID), 
                                            getValue(TOOL_ID), 
                                            getValue(FROM), 
                                            getValue(CONTENT), 
                                            getValue(EVENT), 
                                            getValue(PERSISTENCE_ID), 
                                            getValue(EXPIRATION) == null ? 0 : Long.parseLong(getValue(EXPIRATION)));
    }
    
    // convert org.xmpp.packet.packetExtension to DataSyncPacketExtension
    public static DataSyncPacketExtension convertFromXmppPacketExtension(org.xmpp.packet.PacketExtension xmppPacketExtension) {
        DataSyncPacketExtension dspe = new DataSyncPacketExtension();
        Element peRoot = xmppPacketExtension.getElement();        
        dspe.setValue(TOOL_SESSION_ID, peRoot.element(TOOL_SESSION_ID).getText());
        dspe.setValue(TOOL_ID, peRoot.element(TOOL_ID).getText());
        dspe.setValue(FROM, peRoot.element(FROM).getText());
        dspe.setValue(CONTENT, peRoot.element(CONTENT).getText());
        dspe.setValue(EVENT, peRoot.element(EVENT).getText());
        dspe.setValue(PERSISTENCE_ID, peRoot.element(PERSISTENCE_ID).getText());
        dspe.setValue(EXPIRATION, peRoot.element(EXPIRATION).getText());
        return dspe;
    }


    // convert DataSyncPacketExtension to org.xmpp.packet.packetExtension
    public PacketExtension convertToXmppPacketExtension() {
        PacketExtension pe = new PacketExtension(ELEMENT_NAME, NAMESPACE);
        pe.getElement().addElement(TOOL_SESSION_ID).addText(getValue(TOOL_SESSION_ID));
        pe.getElement().addElement(TOOL_ID).addText(getValue(TOOL_ID));
        pe.getElement().addElement(FROM).addText(getValue(FROM));
        pe.getElement().addElement(CONTENT).addText(getValue(CONTENT));
        pe.getElement().addElement(EVENT).addText(getValue(EVENT));
        pe.getElement().addElement(PERSISTENCE_ID).addText(getValue(PERSISTENCE_ID));
        pe.getElement().addElement(EXPIRATION).addText(getValue(EXPIRATION));
        return pe;
    }

    
    public String toXML() {
        StringBuffer buf = new StringBuffer();        
        buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
        buf.append("<").append(TOOL_SESSION_ID).append(">").append(getValue(TOOL_SESSION_ID)).append("</").append(TOOL_SESSION_ID).append(">");
        buf.append("<").append(TOOL_ID).append(">").append(getValue(TOOL_ID)).append("</").append(TOOL_ID).append(">");
        buf.append("<").append(FROM).append(">").append(getValue(FROM)).append("</").append(FROM).append(">");
        buf.append("<").append(CONTENT).append(">").append(getValue(CONTENT)).append("</").append(CONTENT).append(">");
        buf.append("<").append(EVENT).append(">").append(getValue(EVENT)).append("</").append(EVENT).append(">");
        buf.append("<").append(PERSISTENCE_ID).append(">").append(getValue(PERSISTENCE_ID)).append("</").append(PERSISTENCE_ID).append(">");
        buf.append("<").append(EXPIRATION).append(">").append(getValue(EXPIRATION)).append("</").append(EXPIRATION).append(">");
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

