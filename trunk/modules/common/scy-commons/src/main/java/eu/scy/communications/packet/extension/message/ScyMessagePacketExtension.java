package eu.scy.communications.packet.extension.message;

import org.dom4j.Element;
import org.dom4j.QName;

import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;
import eu.scy.communications.packet.extension.object.ScyObjectPacketExtension;


public class ScyMessagePacketExtension extends ScyObjectPacketExtension implements IScyMessage {
    
    public static final String NAMESPACE = "http://scy.eu/scymessage";
    public static final String ELEMENT_NAME = "scymessage";
    private String description;
    private String name;
    private String id;
    private String userName;
    private String toolName;
    private String to;
    private String session;
    private String objectType;
    private String messagePurpose;
    private String from;
    private long expiraton;
    
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
        
        sb.append("<userName>").append(getUserName()).append("</userName>");
        sb.append("<toolName>").append(getToolName()).append("</toolName>");
        sb.append("<to>").append(getTo()).append("</to>");
        sb.append("<from>").append(getFrom()).append("</from>");
        sb.append("<session>").append(getSession()).append("</session>");
        sb.append("<objectType>").append(getObjectType()).append("</objectType>");
        sb.append("<messagePurpose>").append(getMessagePurpose()).append("</messagePurpose>");
        sb.append("<expiraton>").append(getExpiraton()).append("</expiraton>");
        
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

    @Override
    public long getExpiraton() {
        String elementTextTrim = element.elementTextTrim("expiration");
        return new Long(elementTextTrim).longValue();
    }

    @Override
    public String getFrom() {
        return element.elementTextTrim("from");
    }

    @Override
    public String getMessagePurpose() {
        return element.elementTextTrim("messagePurpose");
    }

    @Override
    public String getObjectType() {
        return element.elementTextTrim("objectType");
    }

    @Override
    public String getSession() {
        return element.elementTextTrim("session");
    }

    @Override
    public String getTo() {
        return element.elementTextTrim("to");
    }

    @Override
    public String getToolName() {
        return element.elementTextTrim("toolName");
    }

    @Override
    public String getUserName() {
        return element.elementTextTrim("userName");
    }

    @Override
    public void setExpiraton(long expiraton) {
        if (element.element("expiraton") != null) {
            element.remove(element.element("expiraton"));
        }
        element.addElement("expiraton").setText(""+expiraton);
        this.expiraton = expiraton;
    }

    @Override
    public void setFrom(String from) {
        if (element.element("from") != null) {
            element.remove(element.element("from"));
        }
        element.addElement("from").setText(from);
        this.from = from;
    }

    @Override
    public void setMessagePurpose(String messagePurpose) {
        if (element.element("messagePurpose") != null) {
            element.remove(element.element("messagePurpose"));
        }
        element.addElement("messagePurpose").setText(messagePurpose);
        this.messagePurpose = messagePurpose;
    }

    @Override
    public void setObjectType(String objectType) {
        if (element.element("objectType") != null) {
            element.remove(element.element("objectType"));
        }
        element.addElement("objectType").setText(objectType);
        this.objectType = objectType;
    }

    @Override
    public void setSession(String session) {
        if (element.element("session") != null) {
            element.remove(element.element("session"));
        }
        element.addElement("session").setText(session);
        this.session = session;
    }

    @Override
    public void setTo(String to) {
        if (element.element("to") != null) {
            element.remove(element.element("to"));
        }
        element.addElement("to").setText(to);
        this.to = to;
    }

    @Override
    public void setToolName(String toolName) {
        if (element.element("toolName") != null) {
            element.remove(element.element("toolName"));
        }
        element.addElement("toolName").setText(toolName);
        this.toolName = toolName;
    }

    @Override
    public void setUserName(String userName) {
        if (element.element("userName") != null) {
            element.remove(element.element("userName"));
        }
        element.addElement("userName").setText(userName);
        this.userName = userName;
    }
    
}
