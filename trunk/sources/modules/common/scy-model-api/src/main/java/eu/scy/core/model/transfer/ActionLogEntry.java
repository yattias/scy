package eu.scy.core.model.transfer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.feb.2011
 * Time: 05:59:25
 * To change this template use File | Settings | File Templates.
 */
public class ActionLogEntry {

    private String tool;
    private String eloURI;
    private String type;

    private List attributes = new LinkedList();

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    public String getEloURI() {
        return eloURI;
    }

    public void setEloURI(String eloURI) {
        this.eloURI = eloURI;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List getAttributes() {
        return attributes;
    }

    public void setAttributes(List attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(ActionLogEntryAttribute actionLogEntryAttribute) {
        getAttributes().add(actionLogEntryAttribute);
    }

    @Override
    public String toString() {
        String returnString = "ACTION: Tool: " + getTool() + "TYPE: " + getType() + " ELO: " + getEloURI() + " Attributes: ";
        for (int i = 0; i < attributes.size(); i++) {
            ActionLogEntryAttribute actionLogEntryAttribute = (ActionLogEntryAttribute) attributes.get(i);
            returnString +="[" + actionLogEntryAttribute.getName() + " :: " + actionLogEntryAttribute.getValue() + "]";
        }
        return returnString;
    }
}
