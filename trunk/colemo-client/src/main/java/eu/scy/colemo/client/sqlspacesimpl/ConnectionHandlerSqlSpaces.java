package eu.scy.colemo.client.sqlspacesimpl;

import eu.scy.colemo.client.ConnectionHandler;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.Tuple;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.jan.2009
 * Time: 23:02:33
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionHandlerSqlSpaces implements ConnectionHandler {

    private TupleSpace tupleSpace = null;

    public void sendMessage(String message) {
        try {
            tupleSpace.write(new Tuple(message, 25));

        } catch (TupleSpaceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void sendObject(Object object) {
        try {
            tupleSpace.write(new Tuple("SEND OBJECT", 25));
        } catch (TupleSpaceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void initialize() throws Exception {
        tupleSpace = new TupleSpace();
    }
}
