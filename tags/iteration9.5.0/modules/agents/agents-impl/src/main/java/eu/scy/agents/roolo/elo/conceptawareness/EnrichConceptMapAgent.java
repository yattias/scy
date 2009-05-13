package eu.scy.agents.roolo.elo.conceptawareness;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
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

public class EnrichConceptMapAgent<T extends IELO<K>, K extends IMetadataKey>
		extends AbstractELOAgent<T, K> {

	private IMetadataKey nodeLabelKey;
	private IMetadataKey linkLabelKey;

	public EnrichConceptMapAgent() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void processElo(T elo) {
		if (elo == null) {
			return;
		}

		IMetadata<K> metadata = elo.getMetadata();
		if (metadata != null) {
			IMetadataValueContainer type = metadata
					.getMetadataValueContainer((K) metadataTypeManager
							.getMetadataKey("type"));
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
			System.err.println("could not parse xml");// e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void enrichMetadata(IMetadata<K> metadata, String xmlString)
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
					.getMetadataValueContainer((K) nodeLabelKey);
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
					.getMetadataValueContainer((K) linkLabelKey);
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
