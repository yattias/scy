package eu.scy.commons.packet.extension;


import org.jivesoftware.smack.packet.PacketExtension;

import eu.scy.core.model.ScyBase;

public class ScyObjectPacketExtension implements PacketExtension, ScyBase {
    
    public static final String NAMESPACE = "http://scy.eu/scy-object";
    public static final String ELEMENT_NAME = "scy-object";
    private String description;
    private String name;
    private String id;
    
    public ScyObjectPacketExtension() {
        super();
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
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setScyBase(ScyBase scyObject){
        this.setId(scyObject.getId());
        this.setDescription(scyObject.getDescription());
        this.setName(scyObject.getName());
    }
    
}
