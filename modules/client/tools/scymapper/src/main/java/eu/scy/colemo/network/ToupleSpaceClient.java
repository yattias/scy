package eu.scy.colemo.network;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.client.TupleSpace;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.des.2008
 * Time: 13:50:31
 * To change this template use File | Settings | File Templates.
 */
public class ToupleSpaceClient {

    private static Logger log = Logger.getLogger("ToupleSpaceClient.class");

    public static void main(String [] argh) {
        try {
            TupleSpace ts = new TupleSpace();

            Tuple t1 = new Tuple("MyFirstTuple", 1);
            ts.write(t1);

            Tuple templateTuple = new Tuple(String.class, Integer.class);
            Tuple returnTuple = ts.take(templateTuple);
            log.info("RETURN : " + returnTuple.getTupleID().getID());
        } catch (TupleSpaceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
