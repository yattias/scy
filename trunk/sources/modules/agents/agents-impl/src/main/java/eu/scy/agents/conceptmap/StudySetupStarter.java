package eu.scy.agents.conceptmap;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.commons.Configuration.Database;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.server.Server;
import eu.scy.agents.api.AgentLifecycleException;

public class StudySetupStarter {

    private  StudyAdminAgent studyAdminAgent;
    private Configuration conf;

    public static void main(String[] args) throws Throwable {
       new StudySetupStarter(args);
//        TupleSpace tsSource = new TupleSpace("scy.collide.info", 2525, "http://www.scy.eu/co2house#");
//        tsSource.exportTuples("tmp.xml");
//        tsSource.disconnect();
//        TupleSpace tsTarget = new TupleSpace("localhost", 2525, "http://www.scy.eu/co2house#");
//        tsTarget.importTuples("tmp.xml");
//        tsTarget.disconnect();
    }

    public StudySetupStarter(String[] args) throws Throwable {
        startServer(args);
        startAgents();
    }

    private void startServer(String[] args) throws TupleSpaceException {
        setConf(Configuration.getConfiguration());
        getConf().setDbType(Database.MYSQL);

//        if (args.length > 0) {
//            conf.setHsqlDestination(args[0]);
//        } else {
//            conf.setHsqlDestination("./database");
//        }

        getConf().setLocal(true);
        getConf().setWebEnabled(false);
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

    private  void startAgents() throws AgentLifecycleException {
        studyAdminAgent = new StudyAdminAgent();
    }


    public  StudyAdminAgent getStudyAdminAgent() {
        return studyAdminAgent;
    }

    private void setConf(Configuration conf) {
        this.conf = conf;
    }

    public Configuration getConf() {
        return conf;
    }

}
