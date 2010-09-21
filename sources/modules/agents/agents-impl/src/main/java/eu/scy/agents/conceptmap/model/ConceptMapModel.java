package eu.scy.agents.conceptmap.model;

import java.util.Properties;

import eu.scy.agents.conceptmapenrich.Graph;

public class ConceptMapModel {
	private static final String PROP_ID = "id";
	private static final String PROP_FROM = "from_node";
	private static final String PROP_TO = "to_node";
	private static final String PROP_LABEL = "name";
	private static final String PROP_NEW_LABEL = "new";

	private String eloURI;

	private Graph graph;

	/**
	 * Constructs a new ConceptMapModel.
	 */
	public ConceptMapModel(String eloUri) {
		this.eloURI = eloUri;
		graph = new Graph();
	}

	public String getEloURI() {
		return eloURI;
	}

	public Graph getGraph() {
		return graph;
	}

	public void nodeAdded(Properties props) {
		graph
				.addNode(props.getProperty(PROP_LABEL), props
						.getProperty(PROP_ID));
	}

	public void nodeRemoved(Properties props) {
		graph.removeNode(props.getProperty(PROP_ID));
	}

	public void edgeAdded(Properties props) {
		graph.addEdge(props.getProperty(PROP_FROM), props.getProperty(PROP_TO),
				props.getProperty(PROP_LABEL), props.getProperty(PROP_ID));
	}

	public void edgeRemoved(Properties props) {
		graph.removeEdge(props.getProperty(PROP_ID));
	}

	public void labelChanged(Properties props) {
		graph.changeLabel(props.getProperty(PROP_ID), props
				.getProperty(PROP_NEW_LABEL));
	}

}
