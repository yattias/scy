package eu.scy.agents.keywords.extractors;

import info.collide.sqlspaces.client.TupleSpace;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import roolo.elo.api.IContent;
import roolo.elo.api.IELO;

public class ConceptMapExtractor implements KeywordExtractor {

	private static final String NAME = "name";
	private static final String NODE = "node";
	private static final String NODES = "nodes";

	// private TupleSpace tuplespace;

	@Override
	public List<String> getKeywords(IELO elo) {
		IContent content = elo.getContent();
		List<String> nodes = extractNodes(content);
		return nodes;
	}

	@SuppressWarnings("unchecked")
	private List<String> extractNodes(IContent content) {
		StringReader stringReader = new StringReader(content.getXmlString());
		SAXBuilder builder = new SAXBuilder();
		Document xmlDocument;
		try {
			xmlDocument = builder.build(stringReader);
			Element rootElement = xmlDocument.getRootElement();
			if (rootElement == null) {
				return Collections.emptyList();
			}

			Set<String> nodeKeywords = new HashSet<String>();
			Element nodesElement = rootElement.getChild(NODES);
			if (nodesElement != null) {
				List<Element> nodes = nodesElement.getChildren(NODE);
				for (Element node : nodes) {
					String nodeLabel = node.getAttributeValue(NAME);
					nodeKeywords.add(nodeLabel);
				}
			}

			return new ArrayList<String>(nodeKeywords);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	@Override
	public TupleSpace getTupleSpace() {
		return null;
	}

	@Override
	public void setTupleSpace(TupleSpace tupleSpace) {
		// do nothing
	}

}
