package eu.scy.agents.impl.elo;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.agents.api.elo.IELOFilterAgent;
import eu.scy.agents.impl.AbstractAgent;

/**
 * Basic implementation of {@link IELOAgent}.
 * 
 * @author Florian Schulz
 * 
 * @param <T>
 * @param <K>
 */
public abstract class AbstractELOAgent<T extends IELO<K>, K extends IMetadataKey>
		extends AbstractAgent implements IELOFilterAgent<T, K> {

	/**
	 * The metadata typemanager needed to change metadata.
	 */
	protected IMetadataTypeManager<IMetadataKey> metadataTypeManager;

	/**
	 * Create a new AbstractELOAgent with <code>name</code> and <code>id</code>.
	 * 
	 * @param name
	 *            The name of the agent.
	 * @param id
	 *            The id of the agent.
	 */
	protected AbstractELOAgent(String name, String id) {
		super(name, id);
	}

	/**
	 * Get the {@link IMetadataTypeManager} used by this filtering agent.
	 * 
	 * @return The used {@link IMetadataTypeManager}.
	 */
	protected IMetadataTypeManager<IMetadataKey> getMetadataTypeManager() {
		return metadataTypeManager;
	}

	/**
	 * Set the needed {@link IMetadataTypeManager} for accessing metadata.
	 * 
	 * @param typeManager
	 *            The needed {@link IMetadataTypeManager} for accessing
	 *            metadata.
	 */
	public void setMetadataTypeManager(
			IMetadataTypeManager<IMetadataKey> typeManager) {
		metadataTypeManager = typeManager;
	}

}
