package eu.scy.agents.impl;

import eu.scy.agents.api.IDecisionAgent;

/**
 * Basic implementation of {@link IDecisionAgent}.
 * 
 * @author Florian Schulz
 * 
 */
public abstract class AbstractDecisionAgent extends AbstractThreadedAgent
		implements IDecisionAgent {

	/**
	 * Create a new {@link AbstractDecisionAgent}.
	 * 
	 * @param name
	 *            The name of the agent.
	 * @param id
	 *            The id of the agent.
	 */
	protected AbstractDecisionAgent(String name, String id) {
		super(name, id);
	}

}
