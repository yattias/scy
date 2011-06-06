package eu.scy.agents.canonical;

import java.util.Map;

import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.PrologAgent;

public class CanonicalAgent extends PrologAgent {

	public CanonicalAgent(Map<String, Object> map) {
		super(CanonicalAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), "canonical_agent.pl",
				"canonical_agent");
	}
	
}
