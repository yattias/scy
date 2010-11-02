package eu.scy.agents.conceptmap;

import java.util.HashMap;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.conceptmap.enrich.CMEnricherAgent;
import eu.scy.agents.conceptmap.model.UserConceptMapAgent;
import eu.scy.agents.conceptmap.proposer.CMProposerAgent;
import eu.scy.agents.impl.AgentProtocol;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.commons.Configuration.Database;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.server.Server;

public class StudySetupStarter {

    public static void main(String[] args) throws Throwable {
        startServer();
//        copyTSContent("http://www.scy.eu/co2house#");
        startAgents();
    }

    public static void copyTSContent(String spaceName) throws TupleSpaceException {
        TupleSpace tsSource = new TupleSpace("scy.collide.info", 2525, spaceName);
        TupleSpace tsTarget = new TupleSpace("localhost", 2525, spaceName);
        tsSource.exportTuples("tmp.xml");
        tsTarget.importTuples("tmp.xml");
    }
    
    private static void startServer() throws TupleSpaceException {
        Configuration conf = Configuration.getConfiguration();
        conf.setDbType(Database.MYSQL);
        conf.setLocal(true);
        conf.setWebEnabled(false);
        Server.startServer();
        TupleSpace ts = new TupleSpace("command");
        ts.deleteAll(new Tuple());
        ts.disconnect();
    }

    private static void startAgents() throws AgentLifecycleException {
        Configuration conf = Configuration.getConfiguration();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(AgentProtocol.TS_HOST, "localhost");
        map.put(AgentProtocol.TS_PORT, conf.getNonSSLPort());

        map.put(AgentProtocol.PARAM_AGENT_ID, "userconceptmap modeller id");
        UserConceptMapAgent a1 = new UserConceptMapAgent(map);
        a1.start();
        
        map.put(AgentProtocol.PARAM_AGENT_ID, "cm enricher id");
        CMEnricherAgent a2 = new CMEnricherAgent(map);
        a2.start();
        
        map.put(AgentProtocol.PARAM_AGENT_ID, "cm proposer oid");
        CMProposerAgent a3 = new CMProposerAgent(map);
        a3.start();
    }

}
