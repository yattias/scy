package eu.scy.communications.packet.extension.message;

import org.dom4j.Element;
import org.dom4j.QName;

import eu.scy.communications.message.ScyMessage;
import eu.scy.communications.packet.extension.object.ScyObjectPacketExtension;


public class ScyMessagePacketExtension extends ScyObjectPacketExtension {
    
    public static final String NAMESPACE = "http://scy.eu/scymessage";
    public static final String ELEMENT_NAME = "scymessage";
    private String description;
    private String name;
    private String id;
    
    static {
        // Register that scy object packet ext uses the jabber:x:data namespace
        registeredExtensions.put(QName.get(ELEMENT_NAME, NAMESPACE), ScyMessagePacketExtension.class);
    }
    
    public ScyMessagePacketExtension() {
        super();
    }

    public ScyMessagePacketExtension(Element element) {
        super(element);
    }
    
    public String toXML() {
        StringBuffer sb = new StringBuffer();
        sb.append("<" + ELEMENT_NAME + " xmlns=\"" + NAMESPACE + "\">");
        sb.append("<id>").append(getId()).append("</id>");
        sb.append("<name>").append(getName()).append("</name>");
        sb.append("<description>").append(getDescription()).append("</description>");
        sb.append("</" + ELEMENT_NAME + ">");
        return sb.toString();
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
    
    public void setScyMessage(ScyMessage scyMessage){
        super.setScyBase(scyMessage);
//        this.setDescription(scyObject.getDescription());
//        this.setName(scyObject.getName());
    }
    
    public ScyMessagePacketExtension createCopy() {
        return new ScyMessagePacketExtension(this.getElement().createCopy());
    }
    
}
