package eu.scy.actionlogging.logger;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.UUID;

import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IContext;
import eu.scy.core.model.impl.ScyBaseObject;

import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class Action extends ScyBaseObject implements IAction {
    
	private Element actionElement;
    private Element contextElement;
    private Element attributesElement;

    public Action(String type, String user) {
    	actionElement = new Element("action");
    	actionElement.setAttribute("id", UUID.randomUUID().toString());
    	actionElement.setAttribute("time", TimeFormatHelper.getInstance().getCurrentTimeMillisAsISO8601());
    	actionElement.setAttribute("type", type);
    	actionElement.setAttribute("user", user);
	
		contextElement = new Element("context");
		actionElement.addContent(contextElement);
		attributesElement = new Element("attributes");
		actionElement.addContent(attributesElement);
    }
    
    public void addContext(String name, String value) {
    	Element prop = new Element("property");
		prop.setAttribute("name", name);
		prop.setAttribute("value", value);
		contextElement.addContent(prop);
    }
    
	public String getContext(String key) {
		String value = null;
		for (Element elem: ((List<Element>)contextElement.getChildren("property"))) {
			if (elem.getAttributeValue("name").equals(key)) {
				value = elem.getAttributeValue("value");
			}
		}
		return value;
	}

    @Override
    public void addContext(ContextConstants constant, String value) {

    }

    @Override
    public String getContext(ContextConstants constant) {
        return null;
    }

    @Override
    public void addAttribute(String name, String value) {	
		if (name != null) {
			Element prop = new Element("property");
			prop.setAttribute("name", name);
			if (value != null) {
				prop.setAttribute("value", value);
			} else {
				prop.setAttribute("value", "");
			}
			attributesElement.addContent(prop);
		}	
    }
	
    public void addAttribute(String name, String value, String subElement) {
		try {
			Document doc = new SAXBuilder().build(new StringReader(subElement));
			doc.getRootElement();
			addAttribute(name, value, doc.getRootElement().detach());
		} catch (JDOMException e) {
			// System.out.println("Action.addAttribute. JDomException caught when adding sub-element.");
			//e.printStackTrace();
		} catch (IOException e) {
			// System.out.println("Action.addAttribute. IOException caught when adding sub-element.");
			//e.printStackTrace();
		}
    }
    
    public void addAttribute(String name, String value, Content subElement) {
    	Element prop = new Element("property");
		prop.setAttribute("name", name);
		prop.setAttribute("value", value);
		prop.addContent(subElement);
		attributesElement.addContent(prop);
    }
	
    @Override
	public String getAttribute(String key) {
    	String value = null;
		for (Element elem: ((List<Element>)attributesElement.getChildren("property"))) {
			if (elem.getAttributeValue("name").equals(key)) {
				value = elem.getAttributeValue("value");
			}
		}
		return value;
	}

    @Override
    public void setContext(IContext context) {

    }

    @Override
    public IContext getContext() {
        return null;
    }

    @Override
    public void setUser(String user) {

    }

    @Override
    public String getUser() {
        return null;
    }

    @Override
    public void setTime(String time) {

    }

    @Override
    public String getTime() {
        return null;
    }

    @Override
    public void setType(String type) {

    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public void setDataType(String dataType) {

    }

    @Override
    public String getDataType() {
        return null;
    }

    @Override
    public void setData(String data) {

    }

    @Override
    public String getData() {
        return null;
    }

    public Action(String xml) throws JDOMException, IOException {
        setFromXML(xml);
    }
    
    public Action(Element element) throws JDOMException {
    	setFromXML(element);
    }
    
    public void setFromXML(String xmlString) throws JDOMException, IOException {
    	Document doc = new SAXBuilder().build(new StringReader(xmlString));
		setFromXML((Element)doc.getRootElement().detach());
    }
    
	public String getXMLString() {
		return new XMLOutputter(Format.getPrettyFormat()).outputString(actionElement);
	}

	public void setFromXML(Element element) throws JDOMException {
		if (element.getName().equals("action")) {
			this.actionElement = element;
			contextElement = element.getChild("context");
	                attributesElement = element.getChild("attributes");
		} else {
			throw new JDOMException("<action> element expected, <"+element.getName()+"> element found.");
		}
	}

    public Element getXML() {
    	return actionElement;
    }
    
    @Override
    public String toString() {
    	return getXMLString();
    }
}
