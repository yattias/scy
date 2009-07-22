package eu.scy.agents.impl.action;

import eu.scy.agents.api.action.IActionLogFilterAgent;
import eu.scy.agents.impl.AbstractAgent;

public abstract class AbstractActionLogFilterAgent extends AbstractAgent
		implements IActionLogFilterAgent {

	public AbstractActionLogFilterAgent(String agentName, String id) {
		super(agentName, id);
	}

}
