package eu.scy.actionlogging.logger;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.UUID;

import eu.scy.actionlogging.api.IAction;
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
    
    @Override
    public void addContext(String name, String value) {
    	Element prop = new Element("property");
		prop.setAttribute("name", name);
		prop.setAttribute("value", value);
		contextElement.addContent(prop);
    }
    
	@Override
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
    public void addAttribute(String name, String value) {
    	Element prop = new Element("property");
		prop.setAttribute("name", name);
		prop.setAttribute("value", value);
		attributesElement.addContent(prop);
    }
	
	@Override
    public void addAttribute(String name, String value, String subElement) {
		try {
			Document doc = new SAXBuilder().build(new StringReader(subElement));
			doc.getRootElement();
			addAttribute(name, value, doc.getRootElement().detach());
		} catch (JDOMException e) {
			System.out.println("Action.addAttribute. JDomException caught when adding sub-element.");
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Action.addAttribute. IOException caught when adding sub-element.");
			//e.printStackTrace();
		}
    }
    
    @Override
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
    
    public Action(String xml) throws JDOMException, IOException {
        setFromXML(xml);
    }
    
    public Action(Element element) throws JDOMException {
    	setFromXML(element);
    }
    
    @Override
    public void setFromXML(String xmlString) throws JDOMException, IOException {
    	Document doc = new SAXBuilder().build(new StringReader(xmlString));
		setFromXML((Element)doc.getRootElement().detach());
    }
    
    @Override
	public String getXMLString() {
		return new XMLOutputter(Format.getPrettyFormat()).outputString(actionElement);
	}

	@Override
	public void setFromXML(Element element) throws JDOMException {
		if (element.getName().equals("action")) {
			this.actionElement = element;
		} else {
			throw new JDOMException("<action> element expected, <"+element.getName()+"> element found.");
		}
	}

    @Override
    public Element getXML() {
    	return actionElement;
    }
}
