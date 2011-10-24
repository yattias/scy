package eu.scy.agents;

import info.collide.sqlspaces.commons.Configuration;
import eu.scy.agents.api.AgentLifecycleException;

public class AgentFramework {

    public static void main(String[] args) {

        Configuration conf = Configuration.getConfiguration();
        try {
            AgentManager am = new AgentManager("localhost", conf.getNonSSLPort());
            for (String s : args) {
                am.startAgent(s, null);
            }
        } catch (AgentLifecycleException e) {
            e.printStackTrace();
        }
    }
}
