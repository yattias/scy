package eu.scy.agents.conceptmap.model;



import java.util.Properties;

import eu.scy.agents.conceptmap.Graph;

public class ConceptMapModel {
	private static final String PROP_ID = "id";
	private static final String PROP_FROM = "from_node";
	private static final String PROP_TO = "to_node";
	private static final String PROP_LABEL = "name";
	private static final String PROP_NEW_LABEL = "new";
	private static final String PROP_SYNONYM = "synonym";

	private String eloURI;

	private Graph graph;

	/**
	 * Constructs a new ConceptMapModel for the EloURI.
	 * 
	 */
	public ConceptMapModel(String eloUri) {
		this(eloUri, new Graph());
	}
	
	/**
	 * Constructs a new ConceptMapModel for the EloURI.
	 */
	public ConceptMapModel(String eloUri, Graph graph) {
		this.eloURI = eloUri;
		this.graph = graph;
		
	}

	public String getEloURI() {
		return eloURI;
	}

	public Graph getGraph() {
		return graph;
	}
	
	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public void nodeAdded(Properties props) {
		graph
				.addNode(props.getProperty(PROP_LABEL), props
						.getProperty(PROP_ID));
	}

	public void synonymAdded(Properties props) {
		graph
				.addNode(props.getProperty(PROP_SYNONYM), props
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

	public void edgeFlipped(Properties props) {		
		graph.removeEdge(props.getProperty(PROP_ID));
		// Attention: here is the flip!
		graph.addEdge(props.getProperty(PROP_TO), props.getProperty(PROP_FROM),
				props.getProperty(PROP_LABEL), props.getProperty(PROP_ID));
	}
	
	public void labelChanged(Properties props) {
		graph.changeLabel(props.getProperty(PROP_ID), props
				.getProperty(PROP_NEW_LABEL));
	}

}
