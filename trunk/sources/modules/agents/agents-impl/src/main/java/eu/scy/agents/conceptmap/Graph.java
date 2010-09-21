package eu.scy.agents.conceptmap;

import info.collide.sqlspaces.commons.Field;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import eu.scy.agents.conceptmap.model.EscapeUtils;
import eu.scy.agents.conceptmap.proposer.Stemmer;

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
        Node fromNode = null;
        Node toNode = null;
        if (useStemming) {
            fromNode = nodes.get(Stemmer.stem(from));
            toNode = nodes.get(Stemmer.stem(to));
        } else {
            fromNode = nodes.get(from);
            toNode = nodes.get(to);
        }
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

    public void fillFromFields(Field[] edgeFields, Field[] nodeFields) {
        for (Field f : nodeFields) {
            String[] s = f.getValue().toString().split(SEPARATOR);
            addNode(s[1], s[0]);
        }
        for (Field f : edgeFields) {
            String[] s = f.getValue().toString().split(SEPARATOR);
            addEdge(s[2], s[3], s[1], s[0]);
        }
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

    public static void main(String[] args) {
        Graph g = new Graph();
        g.addNode("stefan",  "1");
        g.addNode("barbara", "2");
        g.addNode("andrea",  "3");
        g.addNode("thomas",  "4");
        g.addNode("ursula",  "5");
        g.addNode("norbert", "6");
        g.addNode("gerd",    "7");
        g.addNode("marcus",  "8");
        g.addNode("franz",   "9");
        g.addNode("wolfgang","A");
        g.addNode("felix",   "B");
        g.addNode("joris",   "C");
        
        g.addEdge("1", "5", "eltern", "1");
        g.addEdge("2", "5", "eltern", "2");
        g.addEdge("3", "5", "eltern", "3");
        g.addEdge("4", "5", "eltern", "4");
        g.addEdge("1", "6", "eltern", "5");
        g.addEdge("2", "6", "eltern", "6");
        g.addEdge("3", "6", "eltern", "7");
        g.addEdge("4", "6", "eltern", "8");
        g.addEdge("5", "9", "eltern", "9");
        g.addEdge("5", "6", "heirat", "A");
        g.addEdge("2", "7", "heirat", "B");
        g.addEdge("3", "8", "heirat", "C");
        g.addEdge("A", "9", "eltern", "D");
        g.addEdge("B", "A", "eltern", "E");
        g.addEdge("C", "2", "eltern", "F");
        g.addEdge("C", "7", "eltern", "G");
        
//        Node n1 = g.getNode("1");
//        Node n2 = g.getNode("C");
//        int d = n1.getDistance(n2);
        
        Map<Integer, Set<Node>> degreeMap = g.getDegree();
        for (int i : degreeMap.keySet()) {
            System.out.println(i + ": " + degreeMap.get(i));
        }
        
//        Map<Integer, Set<Node>> eccentricityMap = g.getEccentricity();
//        for (int i : eccentricityMap.keySet()) {
//            System.out.println(i + ": " + eccentricityMap.get(i));
//        }

//        Map<Double, Set<Node>> closenessMap = g.getCloseness();
//        for (double i : closenessMap.keySet()) {
//            System.out.println(i + ": " + closenessMap.get(i));
//        }
        
    }

    // TODO swap map dimensions
    public Map<Integer, Set<Node>> getEccentricity() {
        Map<Integer, Set<Node>> m = new TreeMap<Integer, Set<Node>>();
        for (Node n1 : nodes.values()) {
            int e = 0;
            for (Node n2 : nodes.values()) {
                e = Math.max(e, n1.getDistance(n2));
            }
            Set<Node> set = m.get(e);
            if (set == null) {
                set = new HashSet<Node>();
                m.put(e, set);
            }
            set.add(n1);
        }
        return m;
    }
    
    public Map<Node, Integer> getDegree() {
        Map<Node, Integer> m = new TreeMap<Node, Integer>();
        for (Node n1 : nodes.values()) {
            m.put(n1, n1.getEdges().length);
        }
        return m;
    }
    
    // TODO swap map dimensions
    public Map<Double, Set<Node>> getCloseness() {
        Map<Double, Set<Node>> m = new TreeMap<Double, Set<Node>>();
        for (Node n1 : nodes.values()) {
            double c = 0;
            for (Node n2 : nodes.values()) {
                c += n1.getDistance(n2);
            }
            Set<Node> set = m.get(c);
            if (set == null) {
                set = new HashSet<Node>();
                m.put(c, set);
            }
            set.add(n1);
        }
        return m;
    }

    public void addNodeAndEdges(String fromNode, String edge, String toNode) {
        Node fN = nodes.get(fromNode);
        Node tN = nodes.get(toNode);
        if (fN == null) {
            fN = new Node(fromNode, fromNode);
            nodes.put(fromNode, fN);
        }
        if (tN == null) {
            tN = new Node(toNode, toNode);
            nodes.put(toNode, tN);
        }
        addEdge(fromNode, toNode, edge, edge);
    }
    
}
