package eu.scy.agents.conceptmapenrich;

import java.util.HashMap;
import java.util.Map;

public class Graph {

    private Map<String, Node> nodes;

    private Map<String, Edge> edges;

    public Graph() {
        nodes = new HashMap<String, Node>();
        edges = new HashMap<String, Edge>();
    }

    public void addNode(String label) {
        Node node = new Node(label);
        nodes.put(node.getStemmedLabel(), node);
    }

    public void addEdge(String from, String to, String label) {
        Node fromNode = nodes.get(Stemmer.stem(from));
        Node toNode = nodes.get(Stemmer.stem(to));
        Edge edge = new Edge(label, fromNode, toNode);
        edges.put(edge.getStemmedLabel(), edge);
    }

    public Node[] getNodes() {
        return (Node[]) nodes.values().toArray(new Node[nodes.values().size()]);
    }

    public Edge[] getEdges() {
        return (Edge[]) edges.values().toArray(new Edge[edges.values().size()]);
    }

    public Node getNode(String label) {
        return nodes.get(Stemmer.stem(label));
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

	public void removeNode(String property) {
		// TODO mach ma
		
	}

}
