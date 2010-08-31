package eu.scy.agents.conceptmapenrich;

public class Edge {

    private String label;

    private String stemmedLabel;

    private Node fromNode;

    private Node toNode;

    public Edge(String label, Node fromNode, Node toNode) {
        this.label = label;
        this.fromNode = fromNode;
        this.toNode = toNode;
        stemmedLabel = Stemmer.stem(label);
        fromNode.addEdge(this);
        toNode.addEdge(this);
    }

    public Node getFromNode() {
        return fromNode;
    }

    public String getLabel() {
        return label;
    }

    public Node getToNode() {
        return toNode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fromNode == null) ? 0 : fromNode.hashCode());
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        result = prime * result + ((toNode == null) ? 0 : toNode.hashCode());
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
        Edge other = (Edge) obj;
        if (fromNode == null) {
            if (other.fromNode != null)
                return false;
        } else if (!fromNode.equals(other.fromNode))
            return false;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        if (toNode == null) {
            if (other.toNode != null)
                return false;
        } else if (!toNode.equals(other.toNode))
            return false;
        return true;
    }

    public String getStemmedLabel() {
        return stemmedLabel;
    }

    @Override
    public String toString() {
        return "Edge(" + fromNode.getLabel() + " --(" + label + ")--> " + toNode.getLabel() +")";
    }
}
