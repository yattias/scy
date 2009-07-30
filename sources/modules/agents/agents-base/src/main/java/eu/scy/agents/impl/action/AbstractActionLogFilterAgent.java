package eu.scy.agents.impl.action;

import eu.scy.agents.api.action.IActionLogFilterAgent;
import eu.scy.agents.impl.AbstractAgent;

/**
 * Basic implementation of {@link IActionLogFilterAgent}.
 * 
 * @author Florian Schulz
 * 
 */
public abstract class AbstractActionLogFilterAgent extends AbstractAgent
		implements IActionLogFilterAgent {

	/**
	 * Initialize an {@link AbstractActionLogFilterAgent}.
	 * 
	 * @param agentName
	 *            The name of the agent.
	 * @param id
	 *            The id of the agent.
	 */
	public AbstractActionLogFilterAgent(String agentName, String id) {
		super(agentName, id);
	}

}
