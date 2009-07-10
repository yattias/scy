package eu.scy.communications.packet.extension.datasync;

import info.collide.sqlspaces.commons.Field;

import org.dom4j.Element;
import org.dom4j.QName;
import org.xmpp.packet.PacketExtension;

import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.SyncMessageHelper;

/**
 * used by the xmpp message xmlify data sync.
 * 
 * @author anthonjp
 *
 */
public class DataSyncPacketExtension extends PacketExtension implements org.jivesoftware.smack.packet.PacketExtension {

    /**
     * Element name of the packet extension.
     */
    public static final String ELEMENT_NAME = "x";

    /**
     * Namespace of the packet extension.
     */
    public static final String NAMESPACE = "jabber:x:datasync";
    public static final String TOOL_SESSION_ID = "toolSessionId";
    public static final String TOOL_ID = "toolId";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String CONTENT = "content";
    public static final String EVENT = "event";
    public static final String PERSISTENCE_ID = "persistenceId";
    public static final String EXPIRATION = "expiration";
    public static final String TYPE = "DATASYNC";
    
    
    static {
        // Register that DataForms uses the jabber:x:data namespace
        registeredExtensions.put(QName.get(ELEMENT_NAME, NAMESPACE), DataSyncPacketExtension.class);
    }
	
    public DataSyncPacketExtension() {
        super(ELEMENT_NAME, NAMESPACE);
        
    }
    
	public DataSyncPacketExtension(Element element) {
		super(element);
	}

    public DataSyncPacketExtension(ISyncMessage syncMessage) {
        super(ELEMENT_NAME, NAMESPACE);
        
        if( syncMessage.getToolSessionId() != null)
            this.setToolSessionId(syncMessage.getToolSessionId());
        
        if( syncMessage.getToolId() != null)
            this.setToolId(syncMessage.getToolId());
        
        if( syncMessage.getFrom() !=  null)
            this.setFrom(syncMessage.getFrom());
        
        if( syncMessage.getTo() !=  null)
            this.setTo(syncMessage.getTo());
        
        if( syncMessage.getContent() != null)
            this.setContent(syncMessage.getContent());
        
        if( syncMessage.getEvent() != null)
            this.setEvent(syncMessage.getEvent());
        
        if( syncMessage.getPersistenceId() != null)
            this.setPersistenceId(syncMessage.getPersistenceId());
        
        this.setExpiration(syncMessage.getExpiration());
    }
    
    public long getExpiration() {
    	return new Long(element.elementText(EXPIRATION)).longValue(); 	
    }
    
    public void setExpiration(long expiration) {
      	 if (element.element(EXPIRATION) != null) {
            element.remove(element.element(EXPIRATION));
        }
        element.addElement(EXPIRATION).setText(new Long(expiration).toString());
   	}
    
    
    public String getPersistenceId() {
    	return element.elementText(PERSISTENCE_ID); 	
    }
    
    public void setPersistenceId(String persistenceId) {
      	 if (element.element(PERSISTENCE_ID) != null) {
            element.remove(element.element(PERSISTENCE_ID));
        }
        element.addElement(PERSISTENCE_ID).setText(persistenceId);
   	}
    
    public String getEvent() {
    	return element.elementText(EVENT); 	
    }
    
    public void setEvent(String event) {
   	 if (element.element(EVENT) != null) {
         element.remove(element.element(EVENT));
     }
     element.addElement(EVENT).setText(event);
	}

	public String getToolId() {
    	return element.elementText(TOOL_ID); 	
    }
    
    public void setToolId(String toolId) {
    	 if (element.element(TOOL_ID) != null) {
             element.remove(element.element(TOOL_ID));
         }
         element.addElement(TOOL_ID).setText(toolId);
	}
	public String getToolSessionId(){
    	return element.elementText(TOOL_SESSION_ID);
    }
    
    public void setToolSessionId(String toolSessionId) {
    	  if (element.element(TOOL_SESSION_ID) != null) {
              element.remove(element.element(TOOL_SESSION_ID));
          }
          element.addElement(TOOL_SESSION_ID).setText(toolSessionId);
	}

	public String getFrom() {
    	return element.elementText(FROM);
	}
    
    public void setFrom(String from) {
        if (element.element(FROM) != null) {
            element.remove(element.element(FROM));
        }
        element.addElement(FROM).setText(from);
    }
    
    public String getTo() {
        return element.elementText(FROM);
    }
    
    public void setTo(String to) {
        if (element.element(TO) != null) {
            element.remove(element.element(TO));
        }
        element.addElement(TO).setText(to);
    }
    
    
    public String getContent() {
    	return element.elementText(CONTENT);
	}
    
    public void setContent(String content) {
        // Remove an existing title element.
        if (element.element(CONTENT) != null) {
            element.remove(element.element(CONTENT));
        }
        element.addElement(CONTENT).setText(content);
    }
    
    public ISyncMessage toPojo() {

        return SyncMessageHelper.createSyncMessage(
                getToolSessionId() == null || getToolSessionId().equals("null") ? null : getToolSessionId(),
                getToolId() == null || getToolId().equals("null") ? null : getToolId(), 
                getFrom() == null || getFrom().equals("null") ? null : getFrom(), 
                getTo() == null || getTo().equals("null") ? null : getTo(),
                getContent() == null  || getContent().equals("null") ? null : getContent(), 
                getEvent() == null || getEvent().equals("null") ? null : getEvent(), 
                getPersistenceId() == null || getPersistenceId().equals("null") ? null : getPersistenceId(), 
                getExpiration());
    }

    @Override
    public String getElementName() {
       return ELEMENT_NAME;
    }

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    @Override
    public String toXML() {
        StringBuilder xml = new StringBuilder();
        xml.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
        xml.append("<").append(TOOL_SESSION_ID).append(">").append(getToolSessionId()).append("</").append(TOOL_SESSION_ID).append(">");
        xml.append("<").append(TOOL_ID).append(">").append(getToolId()).append("</").append(TOOL_ID).append(">");
        xml.append("<").append(FROM).append(">").append(getFrom()).append("</").append(FROM).append(">");
        xml.append("<").append(TO).append(">").append(getTo()).append("</").append(TO).append(">");
        xml.append("<").append(CONTENT).append(">").append(getContent()).append("</").append(CONTENT).append(">");
        xml.append("<").append(EVENT).append(">").append(getEvent()).append("</").append(EVENT).append(">");
        xml.append("<").append(PERSISTENCE_ID).append(">").append(getPersistenceId()).append("</").append(PERSISTENCE_ID).append(">");
        xml.append("<").append(EXPIRATION).append(">").append(getExpiration()).append("</").append(EXPIRATION).append(">");
        xml.append("</").append(getElementName()).append(">");        
        return xml.toString();
    }
    
	
}
