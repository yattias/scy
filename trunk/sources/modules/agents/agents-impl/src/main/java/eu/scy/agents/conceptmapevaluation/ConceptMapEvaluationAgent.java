package eu.scy.agents.conceptmapevaluation;

import java.util.Map;

import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.PrologAgent;

public class ConceptMapEvaluationAgent extends PrologAgent {

    public ConceptMapEvaluationAgent(Map<String, Object> map) {
        super(ConceptMapEvaluationAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), "ut_agents/applications/ic/agents/concept_map_evaluation/cme_agent.pl", "cme_agent_start([server(local)])");
    }

}
