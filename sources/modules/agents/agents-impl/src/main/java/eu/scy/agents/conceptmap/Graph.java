package eu.scy.agents.conceptmap;

import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.xml.sax.SAXException;

import eu.scy.agents.conceptmap.model.EscapeUtils;
import eu.scy.agents.conceptmap.proposer.Stemmer;

public class Graph {

    private Map<String, Node> nodes;

    private Map<String, Set<Edge>> edges;

    private boolean useStemming;

    public Graph() {
        this(false);
    }

    public Graph(boolean useStemming) {
        nodes = new HashMap<String, Node>();
        edges = new HashMap<String, Set<Edge>>();
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
        String key;
        if (useStemming) {
            key = edge.getStemmedLabel();
        } else {
            key = id;
        }
        Set<Edge> edgeSet = edges.get(key);
        if (edgeSet == null) {
            edgeSet = new HashSet<Edge>();
            edges.put(key, edgeSet);
        }
        edgeSet.add(edge);
    }

    public Node[] getNodes() {
        return nodes.values().toArray(new Node[nodes.values().size()]);
    }

    public Edge[] getEdges() {
        ArrayList<Edge> edgeList = new ArrayList<Edge>();
        for (Set<Edge> es : edges.values()) {
            for (Edge e : es) {
                edgeList.add(e);
            }
        }
        return edgeList.toArray(new Edge[edgeList.size()]);
    }

    public Edge getEdgeForLabels(String from, String to, boolean ignoreCase) {
        Node fromNode = null;
        for (Entry<String, Node> e : nodes.entrySet()) {
            if (ignoreCase && e.getValue().getLabel().equalsIgnoreCase(from)) {
                fromNode = e.getValue();
            } else if (!ignoreCase && e.getValue().getLabel().equals(from)) {
                fromNode = e.getValue();
            }
        }
        if (fromNode != null) {
            for (Edge e : fromNode.getEdges()) {
                if (ignoreCase && e.getToNode().getLabel().equalsIgnoreCase(to)) {
                    return e;
                } else if (!ignoreCase && e.getToNode().getLabel().equals(to)) {
                    return e;
                }
            }
        }
        return null;
    }

    public Node getNode(String labelOrId) {
        if (useStemming) {
            return nodes.get(Stemmer.stem(labelOrId));
        } else {
            return nodes.get(labelOrId);
        }
    }

    /**
     * Returns a {@link info.collide.sqlspaces.commons.Field} array of all nodes. Syntax of one
     * Field is: {@literal id,label}
     * 
     * @return Field array containing all nodes
     */
    public Field[] getNodesAsFields() {
        Node[] nodes = getNodes();
        Field[] fields = new Field[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            String s = EscapeUtils.escape(nodes[i].getId(), nodes[i].getLabel());
            fields[i] = new Field(s);
        }
        return fields;
    }

    /**
     * Returns a {@link info.collide.sqlspaces.commons.Field} array of all edges. Syntax of one
     * Field is: {@literal id,label,fromNodeID,toNodeID}
     * 
     * @return Field array containing all edges
     */
    public Field[] getEdgesAsFields() {
        Edge[] edges = getEdges();
        Field[] fields = new Field[edges.length];
        for (int i = 0; i < edges.length; i++) {
            String s = EscapeUtils.escape(edges[i].getId(), edges[i].getLabel(), edges[i].getFromNode().getId(), edges[i].getToNode().getId());
            fields[i] = new Field(s);
        }
        return fields;
    }

    public void fillFromFields(Field[] edgeFields, Field[] nodeFields) {
        for (Field f : nodeFields) {
            String[] s = f.getValue().toString().split(EscapeUtils.SEPARATOR);
            if (s.length == 1) {
                // workaround for strange split
                s = new String[] { s[0], "" };
            }
            addNode(s[1], s[0]);
        }
        for (Field f : edgeFields) {
            String[] s = f.getValue().toString().split(EscapeUtils.SEPARATOR);
            if (s.length == 1) {
                // workaround for strange split
                s = new String[] { s[0], "" };
            }
            addEdge(s[2], s[3], s[1], s[0]);
        }
    }

    public void saveToFile(String file) throws FileNotFoundException {
        Field[] edgeFields = getEdgesAsFields();
        Field[] nodeFields = getNodesAsFields();
        PrintWriter pw = new PrintWriter(file);
        pw.println(new Tuple(nodeFields).toXMLString());
        pw.println(new Tuple(edgeFields).toXMLString());
        pw.close();
    }
    
    public static Graph loadFromFile(String file) throws IOException, SAXException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String nodesString = br.readLine();
        String edgesString = br.readLine();
        br.close();
        Tuple nodeTuple = Tuple.parseFromXML(nodesString);
        Tuple edgeTuple = Tuple.parseFromXML(edgesString);
        Graph g = new Graph();
        g.fillFromFields(edgeTuple.getFields(), nodeTuple.getFields());
        return g;
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
        for (Set<Edge> es : edges.values()) {
            for (Edge e : es) {
                sb.append("  ");
                sb.append(e);
                sb.append("\n");
            }
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
        Set<Edge> es = edges.get(labelOrId);
        if (es.size() > 1) {
            throw new UnsupportedOperationException();
        }
        Edge e = es.iterator().next();
        if (e != null) {
            if (useStemming) {
                removeEdge(labelOrId);
                addEdge(e.getFromNode().getLabel(), e.getToNode().getLabel(), labelOrId, e.getStemmedLabel());
            } else {
                e.setLabel(label);
            }
            return;
        }
    }

    public TreeMap<Double, Set<Node>> getNodeEccentricity() {
        double min = Integer.MAX_VALUE;
        HashMap<Double, Set<Node>> m = new HashMap<Double, Set<Node>>();
        for (Node n1 : nodes.values()) {
            double e = 0;
            for (Node n2 : nodes.values()) {
                e = Math.max(e, n1.getDistance(n2));
            }
            Set<Node> set = m.get(e);
            if (set == null) {
                set = new HashSet<Node>();
                m.put(e, set);
                min = Math.min(min, e);
            }
            set.add(n1);
        }

        TreeMap<Double, Set<Node>> result = new TreeMap<Double, Set<Node>>(new Comparator<Double>() {

            @Override
            public int compare(Double o1, Double o2) {
                return o2.compareTo(o1);
            }
        });
        for (Entry<Double, Set<Node>> e : m.entrySet()) {
            double key = min / e.getKey();
            result.put(key, e.getValue());
        }
        return result;
    }

    public TreeMap<Double, Set<Node>> getNodeDegree() {
        HashMap<Double, Set<Node>> m = new HashMap<Double, Set<Node>>();
        double max = Integer.MIN_VALUE;
        for (Node n1 : nodes.values()) {
            Set<Node> set = m.get((double) n1.getEdges().length);
            if (set == null) {
                set = new HashSet<Node>();
                m.put((double) n1.getEdges().length, set);
                max = Math.max(max, n1.getEdges().length);
            }
            set.add(n1);
        }
        TreeMap<Double, Set<Node>> result = new TreeMap<Double, Set<Node>>(new Comparator<Double>() {

            @Override
            public int compare(Double o1, Double o2) {
                return o2.compareTo(o1);
            }
        });
        for (Entry<Double, Set<Node>> e : m.entrySet()) {
            double key = e.getKey() / max;
            result.put(key, e.getValue());
        }
        return result;
    }

    public TreeMap<Double, Set<Node>> getNodeCloseness() {
        double min = Integer.MAX_VALUE;
        TreeMap<Double, Set<Node>> m = new TreeMap<Double, Set<Node>>();
        for (Node n1 : nodes.values()) {
            double c = 0;
            for (Node n2 : nodes.values()) {
                c += n1.getDistance(n2);
            }
            Set<Node> set = m.get(c);
            if (set == null) {
                set = new HashSet<Node>();
                m.put(c, set);
                min = Math.min(min, c);
            }
            set.add(n1);
        }
        TreeMap<Double, Set<Node>> result = new TreeMap<Double, Set<Node>>(new Comparator<Double>() {

            @Override
            public int compare(Double o1, Double o2) {
                return o2.compareTo(o1);
            }
        });
        for (Entry<Double, Set<Node>> e : m.entrySet()) {
            double key = min / e.getKey();
            result.put(key, e.getValue());
        }
        return result;
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
