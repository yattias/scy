package eu.scy.communications.packet.extension;

import org.dom4j.Element;
import org.dom4j.QName;
import org.xmpp.packet.PacketExtension;

import eu.scy.core.model.ScyBase;


public class ScyObjectPacketExtension extends PacketExtension implements org.jivesoftware.smack.packet.PacketExtension, ScyBase {
    
    public static final String NAMESPACE = "http://scy.eu/scy-object";
    public static final String ELEMENT_NAME = "scy-object";
    private String description;
    private String name;
    private String id;
    
    static {
        // Register that scy object packet ext uses the jabber:x:data namespace
        registeredExtensions.put(QName.get(ELEMENT_NAME, NAMESPACE), ScyObjectPacketExtension.class);
    }
    
    public ScyObjectPacketExtension() {
        super(ELEMENT_NAME, NAMESPACE);
    }

    public ScyObjectPacketExtension(Element element) {
        super(element);
    }
    
    public String getElementName() {
        return ELEMENT_NAME;
    }
    
    public String getNamespace() {
        return NAMESPACE;
    }
    
    public String toXML() {
        StringBuffer sb = new StringBuffer();
        sb.append("<" + ELEMENT_NAME + " xmlns=\"" + NAMESPACE + "\">");
        sb.append("<id>").append(id).append("</id>");
        sb.append("<name>").append(name).append("</name>");
        sb.append("<description>").append(description).append("</description>");
        sb.append("</" + ELEMENT_NAME + ">");
        return sb.toString();
    }
    
    public String getDescription() {
        return element.elementTextTrim("description");
    }
    
    public void setDescription(String description) {
        if (element.element("description") != null) {
            element.remove(element.element("description"));
        }
        element.addElement("description").setText(description);
        this.description = description;
    }
    
    public String getId() {
        return  element.elementTextTrim("id");
    }
    
    public void setId(String id) {
        // Remove an existing title element.
        if (element.element("id") != null) {
            element.remove(element.element("id"));
        }
        element.addElement("id").setText(id);
        this.id = id;
    }
    
    public String getName() {
        return element.elementTextTrim("name");
    }
    
    public void setName(String name) {
        if (element.element("name") != null) {
            element.remove(element.element("name"));
        }
        element.addElement("name").setText(name);
        this.name = name;
    }
    
    public void setScyBase(ScyBase scyObject){
        this.setId(scyObject.getId());
        this.setDescription(scyObject.getDescription());
        this.setName(scyObject.getName());
    }
    
    public ScyObjectPacketExtension createCopy() {
        return new ScyObjectPacketExtension(this.getElement().createCopy());
    }
    
}
