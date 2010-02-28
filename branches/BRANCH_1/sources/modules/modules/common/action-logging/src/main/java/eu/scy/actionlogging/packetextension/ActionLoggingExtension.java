package eu.scy.actionlogging.packetextension;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.xmpp.packet.PacketExtension;

import eu.scy.actionlogging.api.IAction;

public class ActionLoggingExtension extends PacketExtension implements org.jivesoftware.smack.packet.PacketExtension {

	/**
     * Element name of the packet extension.
     */
    public static final String ELEMENT_NAME = "x";

    /**
     * Namespace of the packet extension.
     */
    public static final String NAMESPACE = "jabber:x:actionlog";
    
    static {
        // Register that DataForms uses the jabber:x:data namespace
        registeredExtensions.put(QName.get(ELEMENT_NAME, NAMESPACE), ActionLoggingExtension.class);
    }
	
	public ActionLoggingExtension(Element element) {
		super(element);
	}
	
	public ActionLoggingExtension() {
		super(ELEMENT_NAME, NAMESPACE);
	}
	
	public ActionLoggingExtension(IAction action) {
		this();
		try {
			Document root = DocumentHelper.parseText(null);/*action.getXMLString()); Please compile before making API changes*/
			element.add((Element) root.getRootElement().detach());
		} catch (DocumentException e) {
			e.printStackTrace();
		}
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
		return element.asXML();
	}

}
