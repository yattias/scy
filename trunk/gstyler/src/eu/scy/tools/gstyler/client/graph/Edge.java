package eu.scy.tools.gstyler.client.graph;

import pl.balon.gwt.diagrams.client.connection.Connection;
import pl.balon.gwt.diagrams.client.connection.StraightTwoEndedConnection;
import pl.balon.gwt.diagrams.client.connector.Connector;
import pl.balon.gwt.diagrams.client.connector.UIObjectConnector;


public class Edge {

    private Node<?, ?> node2;
    private Node<?, ?> node1;
    private Connection connection;

    public Edge(Node<?, ?> node1, Node<?, ?> node2) {
       this.node1 = node1;
       this.node2 = node2;
       Connector c1 = UIObjectConnector.wrap(node1.getNodeView());
       Connector c2 = UIObjectConnector.wrap(node2.getNodeView());
       connection = new StraightTwoEndedConnection(c1, c2);
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
