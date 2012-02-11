package eu.scy.agents.agenda.serialization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import eu.scy.agents.agenda.exception.MissionMapModelException;

import roolo.elo.JDomStringConversion;
import roolo.elo.api.IContent;

public class EloParser {
	
	private static final String ELEMENT_MISSION_MAP_MODEL_ELO_URI = "missionMapModelEloUri";
	private static final String ELEMENT_MISSION_MODEL = "missionModel";
	private static final String ELEMENT_LASSES = "lasses";
	private static final String ELEMENT_LAS = "las";
	private static final String ELEMENT_MAIN_ANCHOR = "mainAnchor";
	private static final String ELEMENT_MAIN_ANCHOR_ELO_URI = "eloUri";
	private static final String ELEMENT_MAIN_ANCHOR_ID = "id";
	private static final String ELEMENT_MAIN_ANCHOR_DEPENDINGS = "dependingOnMissionAnchorIds";
	private static final String ELEMENT_MAIN_ANCHOR_DEPENDING_ON_ID = "dependingOnMissionAnchorId";
	
	private EloParser() {
	}

	/**
	 * Gets the mission map model elo uri from ELO content.
	 * Returns null if not found.
	 *
	 * @param eloContent the ELO content 
	 * @return the mission map model elo uri from content
	 */
	@SuppressWarnings("unchecked")
	public static String getMissionMapModelUriFromMissionRuntimeContent(IContent eloContent) {
		// Example
//		<missionRuntimeElo>
//		  <missionSpecificationEloUri>roolo://scy.collide.info/scy-collide-server/288.288#0</missionSpecificationEloUri>
//		  <missionMapModelEloUri>roolo://scy.collide.info/scy-collide-server/264853.264853#0</missionMapModelEloUri>

    	JDomStringConversion stringConversion = new JDomStringConversion();
    	Element missionSpecElement = stringConversion.stringToXml(eloContent.getXmlString());
    	List<Element> missionSpecChildren = (List<Element>)missionSpecElement.getChildren();
    	for(Element child : missionSpecChildren) {
    		if(child.getName().equals(ELEMENT_MISSION_MAP_MODEL_ELO_URI)) {
    			return child.getText();
    		}
    	}
    	return null;
	}
	
	public static Map<String, BasicMissionAnchorModel> parseMissionMapModel(IContent eloContent) throws MissionMapModelException {
		if(eloContent == null) {
			throw new IllegalArgumentException("eloContent was null");
		}
		JDomStringConversion stringConversion = new JDomStringConversion();
		Element missionModelElement = stringConversion.stringToXml(eloContent.getXmlString());
		return parseMissionMapModel(missionModelElement);
	}
	
	/**
	 * Parses the mission model represented by a MissionModelMap XML document and creates a map 
	 * with BasicMissionAnchorID as key and BasicMissionAnchorModel as value.
	 *
	 * @param eloContent the elo content
	 * @return the map with BasicMissionAnchorID as key and BasicMissionAnchorModel as value
	 * @throws MissionMapModelException the mission model format is invalid
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, BasicMissionAnchorModel> parseMissionMapModel(Element missionModelElement) throws MissionMapModelException {
		// Example
//		<missionModel>
//		  ...
//		  <lasses>
//		    <las>
//		      ...
//			  <mainAnchor>
//			    <eloUri>roolo://scy.collide.info/scy-collide-server/264830.264830#0</eloUri>
//			    <id>healthPassport</id>
//			    ...
//				<dependingOnMissionAnchorIds>
//				  <dependingOnMissionAnchorId>foodAndExerciseDiary</dependingOnMissionAnchorId>

		if(missionModelElement == null) {
			throw new IllegalArgumentException("missionModelElement was null");
		}

		// check root element is 'missionModel'
    	if(!missionModelElement.getName().equals(ELEMENT_MISSION_MODEL)) {
    		throw new MissionMapModelException(String.format(
    				"Content has no '%s' root element. Root element was: %s", 
    				ELEMENT_MISSION_MODEL, 
    				missionModelElement.getName()));
    	}
		
    	// get 'lasses' element
    	Element lassesElement = ParserUtils.getFirstElement(missionModelElement, ELEMENT_LASSES);
    	if(lassesElement == null) {
    		throw new MissionMapModelException(String.format(
    				"%s element has no '%s' element", 
    				ELEMENT_MISSION_MODEL, 
    				ELEMENT_LASSES));
    	}
    	
    	List<Element> lassesChildren = (List<Element>)lassesElement.getChildren();
		Map<String, BasicMissionAnchorModel> anchorMap = new HashMap<String, BasicMissionAnchorModel>(); 
    	for(Element child : lassesChildren) {
    		if(!child.getName().equals(ELEMENT_LAS)) {
    			continue;
    		}
    		Element mainAnchor = ParserUtils.getFirstElement(child, ELEMENT_MAIN_ANCHOR);
    		if(mainAnchor == null) {
        		throw new MissionMapModelException(String.format(
        				"%s element has no '%s' element", 
        				ELEMENT_LAS, 
        				ELEMENT_MAIN_ANCHOR));
    		}
    		BasicMissionAnchorModel bma = new BasicMissionAnchorModel();
    		List<Element> mainAnchorChildren = (List<Element>)mainAnchor.getChildren();
    		for(Element mainAnchorChild : mainAnchorChildren) {
    			if(mainAnchorChild.getName().equals(ELEMENT_MAIN_ANCHOR_ELO_URI)) {
    				bma.setUri(mainAnchorChild.getText());
    			} else if(mainAnchorChild.getName().equals(ELEMENT_MAIN_ANCHOR_ID)) {
    				bma.setId(mainAnchorChild.getText());
    			} else if(mainAnchorChild.getName().equals(ELEMENT_MAIN_ANCHOR_DEPENDINGS)) {
    				List<Element> mainAnchorDependingOn = (List<Element>)mainAnchorChild.getChildren();
    				for(Element dependingOn : mainAnchorDependingOn) {
    					if(dependingOn.getName().equals(ELEMENT_MAIN_ANCHOR_DEPENDING_ON_ID)) {
    						bma.getDependingOnMissionIds().add(dependingOn.getText());
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
	
}
