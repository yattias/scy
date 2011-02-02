package eu.scy.agents.conceptmap;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.commons.Configuration.Database;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.server.Server;
import eu.scy.agents.api.AgentLifecycleException;

public class StudySetupStarter {

    public static void main(String[] args) throws Throwable {
        startServer(args);
        startAgents();
//        TupleSpace tsTarget = new TupleSpace("localhost", 2525, "http://www.scy.eu/co2house#");
//        tsTarget.exportTuples("tmp.xml");
    }

    private static void startServer(String[] args) throws TupleSpaceException {
        Configuration conf = Configuration.getConfiguration();
        conf.setDbType(Database.MYSQL);
//        if (args.length > 0) {
//            conf.setHsqlDestination(args[0]);
//        } else {
//            conf.setHsqlDestination("./database");
//        }

        conf.setLocal(true);
        conf.setWebEnabled(false);
        Server.startServer();
        TupleSpace ts = new TupleSpace("command");
        ts.deleteAll(new Tuple());
        ts.disconnect();
        if (args.length > 0) {
            // System.out.print("Importing tuples ...");
            ts = new TupleSpace("http://www.scy.eu/co2house#");
            ts.importTuples(args[0]);
            ts.disconnect();
            // System.out.println(" Done!");
        }
    }

    private static void startAgents() throws AgentLifecycleException {
        new StudyAdminAgent();
    }

}
