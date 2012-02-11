package eu.scy.agents.agenda.serialization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import roolo.elo.JDomStringConversion;

public class UserAction {
	
	public enum Type {
		MODIFICATION,
		COMPLETION;
	}
	
	public static final String ELE_ROOT = "UserActions";
	
	public static final String ELE_USER_ACTION = "Action";
	
	public static final String ATTR_TIMESTAMP = "timestamp";

	public static final String ATTR_ELOURI = "eloUri";
	
	private long timestamp;
	
	private String eloUri;
	
	private Type actiontype = Type.MODIFICATION;

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
	
	public Type getActiontype() {
		return this.actiontype;
	}

	public void setActiontype(Type actiontype) {
		this.actiontype = actiontype;
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
	
	public static Map<String, UserAction> deserializeUserActions(String actionsAsString) {
		JDomStringConversion stringConversion = new JDomStringConversion();
		Element root = stringConversion.stringToXml(actionsAsString);

		@SuppressWarnings("unchecked")
		List<Element> children = (List<Element>)root.getChildren();
		Map<String, UserAction> userActionMap = new HashMap<String, UserAction>(); 
		for(Element child : children) {
			UserAction userAction = new UserAction();
			userAction.fromXmlElement(child);
			userActionMap.put(userAction.getEloUri(), userAction);
		}
		return userActionMap;
	}
}
