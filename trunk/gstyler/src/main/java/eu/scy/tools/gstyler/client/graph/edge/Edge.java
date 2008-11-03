package eu.scy.tools.gstyler.client.graph.edge;

import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.node.Node;
import pl.balon.gwt.diagrams.client.connection.Connection;
import pl.balon.gwt.diagrams.client.connection.StraightTwoEndedConnection;
import pl.balon.gwt.diagrams.client.connector.Connector;
import pl.balon.gwt.diagrams.client.connector.UIObjectConnector;


public class Edge {

    private Node<?, ?> node2;
    private Node<?, ?> node1;
    private StraightTwoEndedConnection connection;
    private GWTGraph parentGraph;

    /**
     * ATTENTION: Using this constructor requires to call init(node1, node2) right before adding it to a graph
     */
    public Edge() {
    }
    
    /**
     * Creates a new (undirected!) Edge between node1 and node2
     * This edge is valid and can directly be added to a graph
     */
    public Edge(Node<?, ?> node1, Node<?, ?> node2) {
        init(node1, node2);
    }

    /**
     * When the empty constructor was used, this method should be called to make this edge valid, i.e. right before adding it to a graph
     */
    public void init(Node<?, ?> node1, Node<?, ?> node2) {
        this.node1 = node1;
        this.node2 = node2;
        Connector c1 = UIObjectConnector.wrap(node1.getNodeView());
        Connector c2 = UIObjectConnector.wrap(node2.getNodeView());
        connection = new StraightTwoEndedConnection(c1, c2);
        if (getStyleName() != null) {
            connection.addStyleName(getStyleName());
        }
    }
    
    /**
     * Overwrite this method to set the StyleName of the Widget painting the edge, e.g. to change its color
     */
    protected String getStyleName() {
        return null;
    }

    public boolean isValid() {
        return node1 != null && node2 != null && connection != null;
    }

    public Node<?, ?> getNode1() {
        return node1;
    }
    public Node<?, ?> getNode2() {
        return node2;
    }

    public Connection getConnection() {
        return connection;
    }
    
    public GWTGraph getParentGraph() {
        return parentGraph;
    }
    
    /**
     * This method will be called after the Edge was added to a graph
     */
    public void setParentGraph(GWTGraph parentGraph) {
        this.parentGraph = parentGraph;
    }
    
    public Node<?, ?> getOtherNode(Node<?, ?> node) {
        if (getNode1() == node) {
            return getNode2();
        } else if (getNode2() == node) {
            return getNode1();
        }
        return null;
    }
    
    public boolean isConnectedTo(Node<?,?> node) {
        return node.equals(getNode1()) || node.equals(getNode2());
    }
}
