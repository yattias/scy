package eu.scy.agents.votat;

import java.util.HashMap;
import java.util.Map;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.PrologAgent;

public class VotatAgent extends PrologAgent {

	public VotatAgent(Map<String, Object> map) {
		super(VotatAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), "votat_agent.pl", "votat_agent");
	}

	public static void main(String[] args) throws AgentLifecycleException {
		VotatAgent a = new VotatAgent(new HashMap<String, Object>());
		a.start();
	}

}
