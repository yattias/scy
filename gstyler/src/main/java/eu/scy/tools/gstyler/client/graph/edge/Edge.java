package eu.scy.tools.gstyler.client.graph.edge;

import eu.scy.tools.gstyler.client.graph.node.Node;
import pl.balon.gwt.diagrams.client.connection.Connection;
import pl.balon.gwt.diagrams.client.connection.StraightTwoEndedConnection;
import pl.balon.gwt.diagrams.client.connector.Connector;
import pl.balon.gwt.diagrams.client.connector.UIObjectConnector;


public class Edge {

    private Node<?, ?> node2;
    private Node<?, ?> node1;
    private Connection connection;

    public Edge() {
    }
    
    public Edge(Node<?, ?> node1, Node<?, ?> node2) {
        init(node1, node2);
    }

    public void init(Node<?, ?> node1, Node<?, ?> node2) {
        this.node1 = node1;
        this.node2 = node2;
        Connector c1 = UIObjectConnector.wrap(node1.getNodeView());
        Connector c2 = UIObjectConnector.wrap(node2.getNodeView());
        connection = new StraightTwoEndedConnection(c1, c2);
    }
    
    public boolean isValid() {
        return node1 != null && node2 != null && connection != null;
    }
    
    public Node<?, ?> getNode2() {
        return node2;
    }

    public Node<?, ?> getNode1() {
        return node1;
    }

    public Connection getConnection() {
        return connection;
    }
}
