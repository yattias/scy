package eu.scy.agents.api;

import roolo.elo.api.IMetadataKey;

public interface IProcessingAgent<K extends IMetadataKey> extends
		IThreadedAgent {

	/*
	 * Return an instance of the toolbroker api.
	 * 
	 * @return
	 */
	// public ToolBrokerAPI<K> getToolBrokerAPI();
	/*
	 * Set the MetadataTypeManger.
	 */
	// public void setMetadataTypeManager(IMetadataTypeManager<K> typeManager);
	/*
	 * Set the repository to retrieve, search and alter elos.
	 * 
	 * @return
	 */
	// public void setRepository(IRepository<IELO<K>, K> repo);
}
