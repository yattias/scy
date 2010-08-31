package eu.scy.agents.conceptmapenrich;

import java.util.Arrays;
import java.util.HashSet;

public class Node {

    private String label;

    private String stemmedLabel;

    private Edge[] edges;

    public Node(String label) {
        this.label = label;
        stemmedLabel = Stemmer.stemWordWise(label).toLowerCase();
        edges = new Edge[0];
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Node other = (Node) obj;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        return true;
    }

    public Edge[] getEdges() {
        return edges;
    }

    public void addEdge(Edge edge) {
        Edge[] newEdges = (Edge[]) Arrays.copyOf(edges, edges.length + 1);
        newEdges[newEdges.length - 1] = edge;
        edges = newEdges;
    }

    public Node[] getNeighbours(int radius, boolean outgoing, boolean ingoing) {
        HashSet<Node> openList = new HashSet<Node>();
        openList.add(this);
        HashSet<Node> closedList = new HashSet<Node>();
        for (int i = 0; i < radius; i++) {
            HashSet<Node> newOpenList = new HashSet<Node>();
            for (Node n : openList) {
                closedList.add(n);
                for (Edge e : n.getEdges()) {
                    if (e.getFromNode() == n && outgoing && !closedList.contains(e.getToNode())) {
                        newOpenList.add(e.getToNode());
                    }
                    if (e.getToNode() == n && ingoing && !closedList.contains(e.getFromNode())) {
                        newOpenList.add(e.getFromNode());
                    }
                }
            }
            openList = newOpenList;
        }
        closedList.addAll(openList);
        closedList.remove(this);
        return (Node[]) closedList.toArray(new Node[closedList.size()]);
    }

    @Override
    public String toString() {
        return "Node(" + label + ")";
    }

    public String getStemmedLabel() {
        return stemmedLabel;
    }
    
}
