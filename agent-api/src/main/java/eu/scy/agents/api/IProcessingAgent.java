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
	 * Get the repository to retrieve, search and alter elos.
	 * 
	 * @return
	 */
	public IRepository<IELO<K>, K> getRepository();

	/**
	 * Get the MetadataTypeManger.
	 */
	public IMetadataTypeManager<K> getMetadataTypeManager();

}
