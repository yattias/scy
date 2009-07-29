package eu.scy.agents.impl;

import roolo.elo.api.IMetadataKey;
import eu.scy.agents.api.IProcessingAgent;

/**
 * Basic implementation for {@link IProcessingAgent}.
 * 
 * @author Florian Schulz
 * 
 * @param <K>
 */
public abstract class AbstractProcessingAgent<K extends IMetadataKey> extends
		AbstractThreadedAgent implements IProcessingAgent<K> {

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
