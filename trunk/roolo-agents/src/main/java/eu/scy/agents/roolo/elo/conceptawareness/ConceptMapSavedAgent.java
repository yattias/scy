package eu.scy.agents.roolo.elo.conceptawareness;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import roolo.elo.api.I18nType;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.MetadataValueCount;
import roolo.elo.metadata.keys.StringMetadataKey;
import roolo.elo.metadata.value.validators.StringValidator;
import eu.scy.agents.impl.elo.AbstractELOAgent;

public class ConceptMapSavedAgent<T extends IELO<K>, K extends IMetadataKey>
		extends AbstractELOAgent<T, K> {

	private IMetadataKey nodeLabelKey;
	private IMetadataKey linkLabelKey;

	public ConceptMapSavedAgent() {
	}

	private void registerKeys() {
		nodeLabelKey = new StringMetadataKey("nodeLabel",
				"/agentdata/conceptmaps/nodeLabel", I18nType.UNIVERSAL,
				MetadataValueCount.LIST, new StringValidator());
		if (!getMetadataTypeManager().isMetadataKeyRegistered(nodeLabelKey)) {
			getMetadataTypeManager().registerMetadataKey(nodeLabelKey);
		}

		linkLabelKey = new StringMetadataKey("linkLabel",
				"/agentdata/conceptmaps/linkLabel", I18nType.UNIVERSAL,
				MetadataValueCount.LIST, new StringValidator());
		if (!getMetadataTypeManager().isMetadataKeyRegistered(linkLabelKey)) {
			getMetadataTypeManager().registerMetadataKey(linkLabelKey);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processElo(T elo) {
		System.err.println("********************** concept map elo saved ***************");
		if (elo == null) {
			return;
		}

		IMetadata<K> metadata = elo.getMetadata();
		if (metadata != null) {
			IMetadataValueContainer type = metadata
					.getMetadataValueContainer((K) metadataTypeManager
							.getMetadataKey("type"));
			if (!"scy/mapping".equals(type.getValue())) {
				return;
			}
		}

		registerKeys();
		IContent content = elo.getContent();
		try {
			if ((content != null) && (metadata != null)) {

				enrichMetadata(metadata, content.getXmlString());

				TupleSpace ts = getTupleSpace();
				ts.write(new Tuple("scymapper", System.currentTimeMillis(), elo
						.getUri().toString()));
			}
		} catch (TupleSpaceException e) {
			e.printStackTrace();
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
		StringReader stringReader = new StringReader(xmlString);
		SAXBuilder builder = new SAXBuilder();
		Document xmlDocument = builder.build(stringReader);

		Element rootElement = xmlDocument.getRootElement();
		if (rootElement != null) {
			if (!"conceptmap".equals(rootElement.getName())) {
				return;
			}
		}

		Element nodesElement = rootElement.getChild("nodes");
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

		Element linksElement = rootElement.getChild("links");
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
