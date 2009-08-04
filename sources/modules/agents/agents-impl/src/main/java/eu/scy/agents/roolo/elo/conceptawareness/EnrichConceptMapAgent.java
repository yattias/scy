package eu.scy.agents.roolo.elo.conceptawareness;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataValueContainer;
import eu.scy.agents.impl.elo.AbstractELOAgent;

/**
 * A filtering agent that enhances the metadata of ConceptMap ELOs so it is
 * possible to search for similar concept maps.
 * 
 * @see SearchForSimilarConceptsAgent
 * @author Florian Schulz
 * 
 */
public class EnrichConceptMapAgent extends AbstractELOAgent {

	private IMetadataKey nodeLabelKey;
	private IMetadataKey linkLabelKey;

	/**
	 * Create a new EnrichConceptMapAgent filtering agent. The argument
	 * <code>map</code> is used to initialize special parameters.
	 * 
	 * @param map
	 *            Parameters needed to initialize the agent.
	 */
	public EnrichConceptMapAgent(Map<String, Object> map) {
		super("eu.scy.agents.roolo.elo.conceptawareness.EnrichConceptMapAgent",
				(String) map.get("id"));
	}

	@Override
	public void processElo(IELO elo) {
		if (elo == null) {
			return;
		}

		IMetadata metadata = elo.getMetadata();
		if (metadata != null) {
			IMetadataValueContainer type = metadata
					.getMetadataValueContainer(metadataTypeManager
							.getMetadataKey("technicalFormat"));
			if (!"scy/scymapping".equals(type.getValue())) {
				return;
			}
		}

		System.err.println("enriched concept map elo");

		IContent content = elo.getContent();
		try {
			if ((content != null) && (metadata != null)) {

				enrichMetadata(metadata, content.getXmlString());

			}
		} catch (JDOMException e) {
			System.err.println("could not parse xml: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void enrichMetadata(IMetadata metadata, String xmlString)
			throws JDOMException, IOException {
		if (xmlString == null) {
			return;
		}
		nodeLabelKey = metadataTypeManager.getMetadataKey("nodeLabel");
		linkLabelKey = metadataTypeManager.getMetadataKey("linkLabel");

		StringReader stringReader = new StringReader(xmlString);
		SAXBuilder builder = new SAXBuilder();
		Document xmlDocument = builder.build(stringReader);

		Element rootElement = xmlDocument.getRootElement();
		if (rootElement == null) {
			return;
		}

		Element nodesElement = rootElement.getChild("nodes");
		if (nodesElement != null) {
			List<Element> nodes = nodesElement.getChildren("node");
			Set<String> nodeLabels = new HashSet<String>();
			IMetadataValueContainer nodeValue = metadata
					.getMetadataValueContainer(nodeLabelKey);
			for (Element node : nodes) {
				String nodeLabel = node.getAttributeValue("name");
				if (!nodeLabels.contains(nodeLabel)) {
					nodeValue.addValue(nodeLabel);
					nodeLabels.add(nodeLabel);
				}
			}
		}

		Element linksElement = rootElement.getChild("links");
		if (linksElement != null) {
			List<Element> links = linksElement.getChildren("link");
			IMetadataValueContainer linkValue = metadata
					.getMetadataValueContainer(linkLabelKey);
			Set<String> linkLabels = new HashSet<String>();
			for (Element link : links) {
				String linkLabel = link.getAttributeValue("label");
				if (!linkLabels.contains(linkLabel)) {
					linkValue.addValue(linkLabel);
					linkLabels.add(linkLabel);
				}
			}
		}
	}

}
