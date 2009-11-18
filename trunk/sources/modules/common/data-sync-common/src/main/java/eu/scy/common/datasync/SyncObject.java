package eu.scy.common.datasync;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


public class SyncObject implements ISyncObject {

	public static final String PATH = "syncobject"; 
	
	private String id;
	private Map<String, String> properties;
	private String toolname;
	private String userId;
	private long objectCreated;
	private long lastChange = 0;

	public SyncObject(String toolname, String userId) {
		this();
		id = UUID.randomUUID().toString();
		objectCreated = System.currentTimeMillis();
		lastChange = objectCreated;
		this.toolname = toolname;
		//TODO: we should not set the user here but automatically by the sync service!
		this.userId = userId;
	}

	public SyncObject(String xmlString) throws DocumentException {
		this(DocumentHelper.parseText(xmlString).getRootElement());
	}
	
	public SyncObject(Element element) throws DocumentException {
		// make sure we got the right element
		if (!element.getName().equals("syncObject")) {
			throw new DocumentException(
					"cannot parse syncObject, element found was"
					+ element.getName());
		} else {
			// parse the element
			parseContext(element.element("context"));
			parseProperties(element.element("properties"));
		}
	}
	
	/**
	 * Non-argument constructor should only be called by packet transformers!
	 */
	public SyncObject() {
		properties = new HashMap<String, String>();
	}

	private void parseContext(Element contextElement) {
		this.id = contextElement.element("id").getText();
		this.userId = contextElement.element("username").getText();
		this.toolname = contextElement.element("toolname").getText();
		this.objectCreated = Long.parseLong(contextElement.element(
		"objectCreated").getText());
		this.lastChange = Long.parseLong(contextElement.element("lastChange")
				.getText());
	}

	private void parseProperties(Element propertiesElement) {
		properties = new HashMap<String, String>();
		Element property;
		for (Iterator<Element> propertyElements = propertiesElement
				.elementIterator("property"); propertyElements.hasNext();) {
			property = propertyElements.next();
			properties.put(property.attributeValue("name"), property
					.attributeValue("value"));
		}
	}
	
	private Element getContextElement() {
		Element context = DocumentHelper.createElement("context");
		
		Element elem = DocumentHelper.createElement("id");
		elem.setText(this.id);
		context.add(elem);
		
		elem = DocumentHelper.createElement("username");
		elem.setText(this.userId);
		context.add(elem);
		
		elem = DocumentHelper.createElement("toolname");
		elem.setText(this.toolname);
		context.add(elem);
		
		elem = DocumentHelper.createElement("objectCreated");
		elem.setText(""+objectCreated);
		context.add(elem);
		
		elem = DocumentHelper.createElement("lastChange");
		elem.setText(""+lastChange);
		context.add(elem);
		
		return context;
	}
	
	private Element getPropertiesElement() {
		Element propertiesElement = DocumentHelper.createElement("properties");
		Element prop;
		String propKey;
		for (Iterator<String> props = this.properties.keySet().iterator(); props.hasNext();) {
			propKey = props.next();
			prop = DocumentHelper.createElement("property");
			DocumentHelper.createAttribute(prop, propKey, this.properties.get(propKey));
			propertiesElement.add(prop);
		}
		return propertiesElement;
	}
	
	@Override
	public Element toXMLElement() {
		Element syncObject = DocumentHelper.createElement("syncObject");
        syncObject.add(getContextElement());        
        syncObject.add(getPropertiesElement());
        return syncObject;
	}
	
	@Override
	public String toXMLString() {
		return toXMLElement().toString();
	}

	@Override
	public String getID() {
		return id;
	}
	
	

	@Override
	public long getObjectCreatedTime() {
		return this.objectCreated;
	}

	@Override
	public long getLastChangeTime() {
		return this.lastChange;
	}

	@Override
	public Map<String, String> getProperties() {
		return properties;
	}

	@Override
	public String getProperty(String key) {
		return properties.get(key);
	}

	@Override
	public String getToolname() {
		return this.toolname;
	}

	@Override
	public String getUserId() {
		return this.userId;
	}

	@Override
	public void setProperty(String key, String value) {
		this.properties.put(key, value);
	}

	@Override
	public void setLastChangeTime(long timestamp) {
		this.lastChange = timestamp;
	}

	@Override
	public void setObjectCreatedTime(long timestamp) {
		this.objectCreated = timestamp;
	}

	@Override
	public void setToolname(String toolname) {
		this.toolname = toolname;
	}

	@Override
	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public void setID(String id) {
		this.id = id;
	}

}