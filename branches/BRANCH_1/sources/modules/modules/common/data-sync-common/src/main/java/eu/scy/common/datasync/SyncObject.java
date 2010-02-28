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
	
	// string identifier for deleting a property	
	public static final String DELETE = "D3L3737H1S";
	
	private String id;
	private String creator;
	private String lastModificator;
	private long creationTimestamp;
	private long lastModificationTimestamp = 0;
	private String toolname;
	private Map<String, String> properties;

	/**
	 * @param toolname
	 * @param userId
	 * @deprecated Use empty constructor instead
	 */
	@Deprecated
	public SyncObject(String toolname, String userId) {
		this();
		setToolname(toolname);
		setCreator(userId);
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
	
	public SyncObject() {
		properties = new HashMap<String, String>();
		id = UUID.randomUUID().toString();
		creationTimestamp = System.currentTimeMillis();
		lastModificationTimestamp = creationTimestamp;
	}

	private void parseContext(Element contextElement) {
		this.id = getElementContentAsString(contextElement, "id");
		this.creator = getElementContentAsString(contextElement, "creator");
		this.creator = getElementContentAsString(contextElement, "lastmodificator");
		this.toolname = getElementContentAsString(contextElement, "toolname");
		this.creationTimestamp = getElementContentAsLong(contextElement,"creationtimestamp");
		this.lastModificationTimestamp = getElementContentAsLong(contextElement, "lastmodificationtimestamp");
	}
	
	private String getElementContentAsString(Element element, String name) {
		if(element.element(name) != null) {
			return element.element(name).getText();
		}
		return "";
	}
	
	private Long getElementContentAsLong(Element element, String name) {
		String toLong = getElementContentAsString(element, name);
		if(!toLong.equals("")) {
			Long.parseLong(toLong);
		}
 		return Long.MIN_VALUE;
	}

	private void parseProperties(Element propertiesElement) {
		properties = new HashMap<String, String>();
		Element property;
		for (Iterator<Element> propertyElements = propertiesElement.elementIterator("property"); propertyElements.hasNext();) {
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
		
		elem = DocumentHelper.createElement("creator");
		elem.setText(this.creator);
		context.add(elem);

		elem = DocumentHelper.createElement("lastmodificator");
		elem.setText(this.creator);
		context.add(elem);
		
		elem = DocumentHelper.createElement("toolname");
		elem.setText(this.toolname);
		context.add(elem);
		
		elem = DocumentHelper.createElement("creationtimestamp");
		elem.setText(""+creationTimestamp);
		context.add(elem);
		
		elem = DocumentHelper.createElement("lastmodificationtimestamp");
		elem.setText(""+lastModificationTimestamp);
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
	public long getCreationTime() {
		return creationTimestamp;
	}

	@Override
	public long getLastModificationTime() {
		return lastModificationTimestamp;
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
	public String getCreator() {
		return this.creator;
	}
	
	@Override
	public String getLastModificator() {
		return lastModificator;
	}

	@Override
	public void setProperty(String key, String value) {
		this.properties.put(key, value);
	}
	
	public void deleteProperty(String key) {
		this.properties.put(key, DELETE);
	}

	@Override
	public void setLastModificationTime(long timestamp) {
		this.lastModificationTimestamp = timestamp;
	}

	@Override
	public void setCreationTime(long timestamp) {
		this.creationTimestamp = timestamp;
	}

	@Override
	public void setToolname(String toolname) {
		this.toolname = toolname;
	}

	@Override
	public void setCreator(String userId) {
		this.creator = userId;
	}
	
	@Override
	public void setLastModificator(String userId) {
		this.lastModificator = userId;
	}

	@Override
	public void setID(String id) {
		this.id = id;
	}

}