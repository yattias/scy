package eu.scy.agents.impl;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.rmi.dgc.VMID;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import eu.scy.agents.Mission;

public class MissionUriMapping {
	private static final Logger logger = Logger
			.getLogger(MissionUriMapping.class.getName());

	private static Map<String, Set<String>> missionToUri = new HashMap<String, Set<String>>();;
	private static Map<String, String> uriToMission = new HashMap<String, String>();;
	private static boolean mapFilled = false;

	private TupleSpace tupleSpace;

	public MissionUriMapping(TupleSpace tupleSpace) {
		this.tupleSpace = tupleSpace;
	}

	public String getMission(String missionName) {
		// if (!mapFilled) {
		// fillMapping();
		// }
		// return uriToMission.get(missionUri);
		return Mission.getForName(missionName).toString().toLowerCase();
	}

	public Set<String> getMissionUri(String mission) {
		if (!mapFilled) {
			fillMapping();
		}
		return missionToUri.get(mission);
	}

	private synchronized void fillMapping() {
		logger.debug("reading mapping");
		InputStream resourceAsStream = MissionUriMapping.class
				.getResourceAsStream("/missionUriMapping.properties");
		Properties properties = new Properties();
		try {
			properties.load(resourceAsStream);
		} catch (IOException e) {
			mapFilled = true;
			e.printStackTrace();
			return;
		}
		for (String missionUri : properties.stringPropertyNames()) {
			String missionValue = properties.getProperty(missionUri);
			lookupMissionUri(missionUri, missionValue);

			addToMapping(missionUri, missionValue);
		}
		mapFilled = true;
	}

	private void lookupMissionUri(String missionUri, String missionValue) {
		VMID queryId = new VMID();
		Tuple tuple = new Tuple(queryId.toString(), "roolo-agent", "elo",
				missionUri);
		try {
			tupleSpace.write(tuple);
			Tuple response = tupleSpace.waitToTake(new Tuple(
					queryId.toString(), "roolo-response", String.class),
					AgentProtocol.SECOND * 10);
			if (response == null) {
				return;
			}
			String eloXML = (String) response.getField(2).getValue();
			SAXBuilder builder = new SAXBuilder();
			Document eloDocument = builder.build(new StringReader(eloXML));

			List<Element> nodes = getIdentifier(eloDocument);
			for (Element identifierNode : nodes) {
				String eloIdentifier = identifierNode.getTextTrim();
				if (!eloIdentifier.equals(missionUri)) {
					addToMapping(eloIdentifier, missionValue);
				}
			}
		} catch (TupleSpaceException e) {
			logger.warn("elo " + missionUri + " not found", e);
			return;
		} catch (JDOMException e) {
			logger.warn("elo " + missionUri + " not found", e);
			return;
		} catch (IOException e) {
			logger.warn("elo " + missionUri + " not found", e);
			return;
		}
	}

	private void addToMapping(String missionUri, String missionValue) {
		uriToMission.put(missionUri, missionValue);
		Set<String> missionUris = missionToUri.get(missionValue);
		if (missionUris == null) {
			missionUris = new HashSet<String>();
		}
		missionUris.add(missionUri);
		missionToUri.put(missionValue, missionUris);
	}

	@SuppressWarnings("unchecked")
	private List<Element> getIdentifier(Document eloDocument) {
		Element eloElement = eloDocument.getRootElement();
		Element metadataElement = eloElement.getChild("metadata");
		Element lomElement = metadataElement.getChild("lom");
		Element generalElement = lomElement.getChild("general");
		Element identifierElement = generalElement.getChild("identifier");
		return identifierElement.getChildren("entry");
	}
}
