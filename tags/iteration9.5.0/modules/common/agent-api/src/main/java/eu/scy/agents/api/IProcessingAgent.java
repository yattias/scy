package eu.scy.agents.api;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;

public interface IProcessingAgent<K extends IMetadataKey> extends IAgent,
		Runnable {

	/**
	 * Run the agent.
	 */
	public void run();

	/**
	 * Set the MetadataTypeManger.
	 */
	public void setMetadataTypeManager(IMetadataTypeManager<K> typeManager);

	/**
	 * Set the repository to retrieve, search and alter elos.
	 * 
	 * @return
	 */
	public void setRepository(IRepository<IELO<K>, K> repo);

}
