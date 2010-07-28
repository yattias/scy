package eu.scy.agents.serviceprovider.ontology;

import java.util.Map;

import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.PrologAgent;

public class OntologyLookupAgent extends PrologAgent {

	public OntologyLookupAgent(Map<String, Object> map) {
		super(OntologyLookupAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), "ontology_agent.pl",
				"start_agent(onto, 2525)");
	}

}
