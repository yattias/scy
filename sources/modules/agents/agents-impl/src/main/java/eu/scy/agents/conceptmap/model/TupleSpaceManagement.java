package eu.scy.agents.conceptmap.model;

import java.util.HashMap;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;

public class TupleSpaceManagement {

	public static void main(String[] args) {
		
		try {
			HashMap<String, Object> m = new HashMap<String, Object>();
			m.put(AgentProtocol.PARAM_AGENT_ID, "a nice little id should be inserted here");
			m.put(AgentProtocol.TS_HOST, "127.0.0.1");
			m.put(AgentProtocol.TS_PORT, 2525);
			UserConceptMapAgent agent = new UserConceptMapAgent(m);
			agent.start();
			System.out.println("Agent started");
				
		} catch (AgentLifecycleException e) {
			System.out.println("Error in AgentLifecycle");
		}
	}
}
