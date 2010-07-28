package eu.scy.agents.impl;

import eu.scy.agents.api.IProcessingAgent;

/**
 * Basic implementation for {@link IProcessingAgent}.
 * 
 * @author Florian Schulz
 * 
 * @param <K>
 */
public abstract class AbstractProcessingAgent extends AbstractThreadedAgent
		implements IProcessingAgent {

	/**
	 * Create a new {@link AbstractProcessingAgent}.
	 * 
	 * @param name
	 *            The name of the agent.
	 * @param id
	 *            The id of the agent.
	 */
	public AbstractProcessingAgent(String name, String id) {
		super(name, id);
	}

}
