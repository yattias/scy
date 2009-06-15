package eu.scy.datasync.extension;

import org.dom4j.Element;
import org.xmpp.packet.PacketExtension;

import eu.scy.datasync.api.ISyncMessage;

/**
 * used by the xmpp message xmlify data sync.
 * 
 * @author anthonjp
 *
 */
public class DataSyncPacketExtension extends PacketExtension {

	
    /**
     * Element name of the packet extension.
     */
    public static final String ELEMENT_NAME = "datasync";

    /**
     * Namespace of the packet extension.
     */
    public static final String NAMESPACE = "SyncMessage.DATA_SYNC_XMPP_NAMESPACE";
	
	public DataSyncPacketExtension(Element element) {
		super(element);
	}

    public DataSyncPacketExtension(ISyncMessage syncMessage) {
        super(ELEMENT_NAME, NAMESPACE);
        
        this.setToolSessionId(syncMessage.getToolSessionId());
        this.setToolId(syncMessage.getToolId());
        this.setFrom(syncMessage.getFrom());
        this.setContent(syncMessage.getContent());
        this.setEvent(syncMessage.getEvent());
        this.setExpiration(syncMessage.getExpiration());
    }
    
    public long getExpiration() {
    	return new Long(element.elementText("Expiration")).longValue(); 	
    }
    
    public void setExpiration(long expiration) {
      	 if (element.element("Expiration") != null) {
            element.remove(element.element("Expiration"));
        }
        element.addElement("Expiration").setText(new Long(expiration).toString());
   	}
    
    
    public String getPersistenceId() {
    	return element.elementText("persistenceId"); 	
    }
    
    public void setPersistenceId(String persistenceId) {
      	 if (element.element("persistenceId") != null) {
            element.remove(element.element("persistenceId"));
        }
        element.addElement("persistenceId").setText(persistenceId);
   	}
    
    public String getEvent() {
    	return element.elementText("event"); 	
    }
    
    public void setEvent(String event) {
   	 if (element.element("event") != null) {
         element.remove(element.element("event"));
     }
     element.addElement("event").setText(event);
	}

	public String getToolId() {
    	return element.elementText("toolId"); 	
    }
    
    public void setToolId(String toolId) {
    	 if (element.element("toolId") != null) {
             element.remove(element.element("toolId"));
         }
         element.addElement("toolId").setText(toolId);
	}
	public String getToolSessionId(){
    	return element.elementText("toolSessionId");
    }
    
    public void setToolSessionId(String toolSessionId) {
    	  if (element.element("toolSessionId") != null) {
              element.remove(element.element("toolSessionId"));
          }
          element.addElement("toolSessionId").setText(toolSessionId);
	}

	public String getFrom() {
    	return element.elementText("from");
	}
    
    public void setFrom(String from) {
        if (element.element("from") != null) {
            element.remove(element.element("from"));
        }
        element.addElement("from").setText(from);
    }
    
    
    public String getContent() {
    	return element.elementText("content");
	}
    
    public void setContent(String content) {
        // Remove an existing title element.
        if (element.element("content") != null) {
            element.remove(element.element("content"));
        }
        element.addElement("content").setText(content);
    }
    
    
    
	
}
