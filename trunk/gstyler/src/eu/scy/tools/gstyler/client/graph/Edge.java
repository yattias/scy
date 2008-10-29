package eu.scy.tools.gstyler.client.graph;


public class Edge {

    private Node<?, ?> node2;
    private Node<?, ?> node1;

    public Edge(Node<?, ?> node1, Node<?, ?> node2) {
       this.node1 = node1;
       this.node2 = node2;
    }
    
    public Node<?, ?> getNode2() {
        return node2;
    }

    public Node<?, ?> getNode1() {
        return node1;
    }
}
