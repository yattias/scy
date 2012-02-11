package eu.scy.agents.agenda.serialization;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

public class ParserUtils {

	@SuppressWarnings("unchecked")
	public static Element getFirstElement(Element root, String elementName) {
		List<Element> children = (List<Element>)root.getChildren();
		for(Element child : children) {
			if(child.getName().equals(elementName)) {
				return child;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Element getFirstElement(Element root, String elementName, String attributeKey, String attributeValue) {
		List<Element> children = (List<Element>)root.getChildren();
		for(Element child : children) {
			String value = child.getAttributeValue(attributeKey);
			if(value != null && value.equals(attributeValue)) {
				return child;
			}
		}
		return null;
	}
		
	@SuppressWarnings("unchecked")
	public static List<Element> getElements(Element root, String elementName, String attributeKey, String attributeValue) {
		List<Element> result = new ArrayList<Element>();
		List<Element> properties = (List<Element>)root.getChildren();
		for(Element property : properties) {
			if(property.getName().equals(elementName)) {
				String value = property.getAttributeValue(attributeKey);
				if(value != null && value.equals(attributeValue)) {
					result.add(property);
				}
			}
		}
		return result;
	}
	
}
