package eu.scy.agents.api;

/**
 * A processing agent is a special type of threaded agent that allows for heavy
 * number crunching where the result of the calculation is not needed
 * immediately and will be reported to the TupleSpace. The results can then be
 * used by another agent. Processing agents only communicate with the TupleSpace
 * and the {@link IPersistentStorage}
 * 
 * @author Florian Schulz
 * 
 */
public interface IProcessingAgent extends IThreadedAgent {

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
