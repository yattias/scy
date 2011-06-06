package eu.scy.agents.impl;

import java.util.Map;

import eu.scy.agents.api.IRequestAgent;

public abstract class AbstractRequestAgent extends AbstractThreadedAgent
		implements IRequestAgent {

	public AbstractRequestAgent(String name, Map<String, Object> params) {
		this(name, (String) params.get(AgentProtocol.PARAM_AGENT_ID));
	}

	public AbstractRequestAgent(String name, String id) {
		super(name, id);
	}

}
