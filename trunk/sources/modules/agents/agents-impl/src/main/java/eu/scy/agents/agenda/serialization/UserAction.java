package eu.scy.agents.agenda.serialization;

import org.jdom.Element;

public class UserAction {
	
	public static final String ELE_ROOT = "UserActions";
	
	public static final String ELE_USER_ACTION = "Action";
	
	public static final String ATTR_TIMESTAMP = "timestamp";

	public static final String ATTR_ELOURI = "eloUri";
	
	private long timestamp;
	
	private String eloUri;

	public UserAction() {
		this.timestamp = Long.MIN_VALUE;
		this.eloUri = null;
	}
	
	public UserAction(long timestamp, String eloUri) {
		this.timestamp = timestamp;
		this.eloUri = eloUri;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public String getEloUri() {
		return this.eloUri;
	}

	public Element toXmlElement() {
    	Element action = new Element(ELE_USER_ACTION);
    	action.setAttribute(ATTR_TIMESTAMP, String.valueOf(this.timestamp));
    	action.setAttribute(ATTR_ELOURI, this.eloUri);
    	return action;
	}
	
	public void fromXmlElement(Element action) {
		this.timestamp = Long.valueOf(action.getAttributeValue(ATTR_TIMESTAMP));
		this.eloUri = action.getAttributeValue(ATTR_ELOURI);
	}
	
}
