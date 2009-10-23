package eu.scy.server.startup;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.server.Server;

import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.okt.2009
 * Time: 06:00:03
 * To change this template use File | Settings | File Templates.
 */
public class SQLSpacesStarter {


        public SQLSpacesStarter() {
        new Thread() {
            @Override
            public void run() {
                log.info("Initializing SQL SPACES SERVER");
                Configuration.getConfiguration().setLogLevel(Level.INFO);
                Configuration.getConfiguration().setDbType(Configuration.Database.MYSQL);
                Configuration.getConfiguration().setMysqlHost("localhost");
                Configuration.getConfiguration().setMysqlPort(3306);
                Configuration.getConfiguration().setMysqlSchema("sqlspaces");
                Configuration.getConfiguration().setDbUser("sqlspaces");
                Configuration.getConfiguration().setDbPassword("sqlspaces");
                Configuration.getConfiguration().setWebPort(8091);
                Configuration.getConfiguration().setWebEnabled(false);
                Configuration.getConfiguration().setWebServicesEnabled(false);
                Configuration.getConfiguration().setWebRoot("/home/scy/sqls");
                Server.startServer();

                try {
                    TupleSpace ts = new TupleSpace();

                    Tuple t1 = new Tuple("MyFirstTuple", 1);
                    ts.write(t1);

                    Tuple templateTuple = new Tuple(String.class, Integer.class);
                    Tuple returnTuple = ts.take(templateTuple);
                    System.out.println("Return tuple: " +returnTuple);


                } catch (TupleSpaceException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    private Logger log = Logger.getLogger("SQLSpacesStarter.class");



}
