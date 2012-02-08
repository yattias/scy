package eu.scy.agents.agenda.serialization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

import eu.scy.agents.agenda.exception.InvalidMissionSpecificationException;


public class MissionSpecificationParser {

	private static final String ELEMENT_BEAN = "bean";
	private static final String ATTRIBUTE_BEAN_NAME = "name";
	private static final String ATTRIBUTE_BEAN_NAME_VALUE = "missionConfigInput";
	private static final String ELEMENT_PROPERTY = "property";
	private static final String ATTRIBUTE_PROPERTY_NAME = "name";
	private static final String ATTRIBUTE_PROPERTY_NAME_VALUE = "basicMissionAnchors";
	private static final String ELEMENT_LIST = "list";
	private static final String ATTRIBUTE_CLASS = "class";
	private static final String ATTRIBUTE_CLASS_BMA = "eu.scy.client.desktop.scydesktop.config.BasicMissionAnchor";
	private static final String ATTRIBUTE_NAME_ID = "id";
	private static final String ATTRIBUTE_NAME_URI = "uri";
	private static final String ATTRIBUTE_VALUE = "value";
	private static final String ATTRIBUTE_DEPENDING_ON_MISSION = "dependingOnMissionAnchorIds";
	private static final String ELEMENT_VALUE = "value";
	
	/**
	 * Parses the XML document and creates a map with BasicMissionAnchor ID as key 
	 * and BasicMissionAnchor ID as value.
	 *
	 * @param doc the mission specification as xml doc
	 * @return the map
	 * @throws InvalidMissionSpecificationException mission specification file was not valid
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, BasicMissionAnchorModel> parse(Document doc) throws InvalidMissionSpecificationException {
		Element root = doc.getRootElement();
		if(root == null) {
			throw new InvalidMissionSpecificationException("XML Document has no root!");
		}
		
		Element missionConfigInputBean = getSingleElement(
				root, 
				ELEMENT_BEAN, 
				ATTRIBUTE_BEAN_NAME, 
				ATTRIBUTE_BEAN_NAME_VALUE);
		if(missionConfigInputBean == null) {
			throw new InvalidMissionSpecificationException("Top level bean element is missing!");
		}
		
		Element basicMissionAnchorProperty = getSingleElement(
				missionConfigInputBean, 
				ELEMENT_PROPERTY, 
				ATTRIBUTE_PROPERTY_NAME, 
				ATTRIBUTE_PROPERTY_NAME_VALUE);
		if(basicMissionAnchorProperty == null) {
			throw new InvalidMissionSpecificationException("Property element with name 'basicMissionAnchor' is missing!");
		}
		
		Element listElement = getFirstElement(basicMissionAnchorProperty, ELEMENT_LIST); 
		if(listElement == null) {
			throw new InvalidMissionSpecificationException("List element is missing!");
		}
		
		List<Element> basicMissionAnchorList = getElements(listElement, ELEMENT_BEAN, ATTRIBUTE_CLASS, ATTRIBUTE_CLASS_BMA);

		Map<String, BasicMissionAnchorModel> anchorMap = new HashMap<String, BasicMissionAnchorModel>(); 
		for(Element anchorBean : basicMissionAnchorList) {
			BasicMissionAnchorModel bma = new BasicMissionAnchorModel();

			List<Element> anchorProperties = (List<Element>)anchorBean.getChildren();
			for(Element prop : anchorProperties) {
				if(prop.getName().equals(ELEMENT_PROPERTY)) {
					if(prop.getAttributeValue(ATTRIBUTE_PROPERTY_NAME).equals(ATTRIBUTE_NAME_ID)) {
						bma.setId(prop.getAttributeValue(ATTRIBUTE_VALUE));
					} else if(prop.getAttributeValue(ATTRIBUTE_PROPERTY_NAME).equals(ATTRIBUTE_NAME_URI)) {
						bma.setUri(prop.getAttributeValue(ATTRIBUTE_VALUE));
					} else if(prop.getAttributeValue(ATTRIBUTE_PROPERTY_NAME).equals(ATTRIBUTE_DEPENDING_ON_MISSION)) {
						Element list = getFirstElement(prop, ELEMENT_LIST);
						if(list == null) {
							continue;
						}
						List<Element> missionIds = (List<Element>)list.getChildren();
						for(Element missionId : missionIds) {
							if(missionId.getName().equals(ELEMENT_VALUE)) {
								bma.getDependingOnMissionIds().add(missionId.getText());
							}
						}
					}
				}
			}
			if(bma.getId() != null && bma.getUri() != null) {
				anchorMap.put(bma.getId(), bma);
			}
		}
		return anchorMap;
	}
	
	@SuppressWarnings("unchecked")
	private static Element getFirstElement(Element root, String elementName) {
		List<Element> children = (List<Element>)root.getChildren();
		for(Element child : children) {
			if(child.getName().equals(elementName)) {
				return child;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private static Element getSingleElement(Element root, String elementName, String attributeKey, String attributeValue) {
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
	private static List<Element> getElements(Element root, String elementName, String attributeKey, String attributeValue) {
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
	
//	public static void main(String[] args) {
//		try {
//			URL resource = MissionSpecificationParser.class.getResource("/pizzaMission.xml");
//			Document doc = new SAXBuilder().build(resource.toString());
//			Map<String, BasicMissionAnchorModel> anchorMap = MissionSpecificationParser.parse(doc);
//			for(String id : anchorMap.keySet()) {
//				System.out.println(anchorMap.get(id).toString());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
