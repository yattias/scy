package eu.scy.agents.conceptmapenrich;

import info.collide.sqlspaces.commons.Field;

import java.util.HashMap;
import java.util.Map;

import eu.scy.agents.conceptmap.model.EscapeUtils;

public class Graph {

    private Map<String, Node> nodes;

    private Map<String, Edge> edges;

    private boolean useStemming;

    public Graph() {
        this(false);
    }

    public Graph(boolean useStemming) {
        nodes = new HashMap<String, Node>();
        edges = new HashMap<String, Edge>();
        this.useStemming = useStemming;
    }

    public void addNode(String label, String id) {
        Node node = new Node(label, id);
        if (useStemming) {
            nodes.put(node.getStemmedLabel(), node);
        } else {
            nodes.put(id, node);
        }
    }

    public void addEdge(String from, String to, String label, String id) {
        Node fromNode = nodes.get(Stemmer.stem(from));
        Node toNode = nodes.get(Stemmer.stem(to));
        Edge edge = new Edge(label, fromNode, toNode, id);
        if (useStemming) {
            edges.put(edge.getStemmedLabel(), edge);
        } else {
            edges.put(id, edge);
        }
    }

    public Node[] getNodes() {
        return (Node[]) nodes.values().toArray(new Node[nodes.values().size()]);
    }

    public Edge[] getEdges() {
        return (Edge[]) edges.values().toArray(new Edge[edges.values().size()]);
    }

    public Node getNode(String labelOrId) {
        if (useStemming) {
            return nodes.get(Stemmer.stem(labelOrId));
        } else {
            return nodes.get(labelOrId);
        }
    }

	/**
	 * Returns a {@link info.collide.sqlspaces.commons.Field} array of all
	 * nodes. Syntax of one Field is: {@literal id,label}
	 * 
	 * @return Field array containing all nodes
	 */
	public Field[] getNodesAsFields() {
		Node[] nodes = getNodes();
		Field[] fields = new Field[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			String s = EscapeUtils.escape(
					nodes[i].getId(), nodes[i].getLabel());
//			String s = nodes[i].getId() + SEPARATOR + nodes[i].getLabel();
			fields[i] = new Field(s);
		}
		return fields;
	}

	/**
	 * Returns a {@link info.collide.sqlspaces.commons.Field} array of all
	 * edges. Syntax of one Field is: {@literal id,label,fromNodeID,toNodeID}
	 * 
	 * @return Field array containing all edges
	 */
	public Field[] getEdgesAsFields() {
		Edge[] edges = getEdges();
		Field[] fields = new Field[edges.length];
		for (int i = 0; i < edges.length; i++) {
			String s = EscapeUtils.escape(
					edges[i].getId(), edges[i].getLabel(), 
					edges[i].getFromNode().getId(), edges[i].getToNode().getId()
					);

//			String s = edges[i].getId() + SEPARATOR + edges[i].getLabel() + SEPARATOR
//					+ edges[i].getFromNode().getId() + SEPARATOR + edges[i].getToNode().getId();
			fields[i] = new Field(s);
		}
		return fields;
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nodes:\n");
        for (Node n : nodes.values()) {
            sb.append("  ");
            sb.append(n);
            sb.append("\n");
        }
        sb.append("Edges:\n");
        for (Edge e : edges.values()) {
            sb.append("  ");
            sb.append(e);
            sb.append("\n");
        }
        return sb.toString();
    }

    public void removeNode(String labelOrId) {
        nodes.remove(labelOrId);
    }

    public void removeEdge(String labelOrId) {
        edges.remove(labelOrId);
    }
    
    public void changeLabel(String labelOrId, String label) {
        Node n = nodes.get(labelOrId);
        if (n != null) {
            if (useStemming) {
                nodes.remove(labelOrId);
                n.setLabel(label);
                nodes.put(n.getStemmedLabel(), n);
            } else {
                n.setLabel(label);
            }
            return;
        } 
        Edge e = edges.get(labelOrId);
        if (e != null) {
            if (useStemming) {
                edges.remove(labelOrId);
                e.setLabel(label);
                edges.put(e.getStemmedLabel(), e);
            } else {
                e.setLabel(label);
            }
            return;
        } 
    }

}
