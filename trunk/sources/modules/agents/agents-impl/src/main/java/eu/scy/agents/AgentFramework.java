package eu.scy.agents;

import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.commons.Configuration.Database;
import info.collide.sqlspaces.server.Server;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.manager.AgentManager;

public class AgentFramework {

    public static void main(String[] args) {
        Configuration conf = Configuration.getConfiguration();
        conf.setDbType(Database.HSQL);
        conf.setWebEnabled(false);
        conf.setWebServicesEnabled(false);
        conf.setSSLEnabled(false);
        Server.startServer();
        try {
            AgentManager am = new AgentManager("localhost", conf.getNonSSLPort());
            for (String s : args) {
                am.startAgent(s, null);
            }
        } catch (AgentLifecycleException e) {
            e.printStackTrace();
            Server.stopServer();
        }
    }

}
