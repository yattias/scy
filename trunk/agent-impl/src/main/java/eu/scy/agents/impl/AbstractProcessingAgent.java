package eu.scy.agents.impl;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.agents.api.IProcessingAgent;

public abstract class AbstractProcessingAgent<K extends IMetadataKey> extends
		AbstractAgent implements IProcessingAgent<K> {

	protected boolean done;
	protected IRepository<IELO<K>, K> repository;
	protected IMetadataTypeManager<K> metadataTypeManager;

	// private ToolBrokerAPI<K> toolBrokerAPI;

	@Override
	public void run() {
		while (!done) {
			doRun();
		}
	}

	public AbstractProcessingAgent(String threadName) {
		// this(threadName);, new ToolBrokerImpl<K>());
		super();
		done = false;
		Thread t = new Thread(this, threadName);
		t.start();
	}

	// protected AbstractProcessingAgent(String threadName,
	// ToolBrokerAPI<K> toolBroker) {
	// toolBrokerAPI = toolBroker;
	// done = false;
	// Thread t = new Thread(this, threadName);
	// t.start();
	// }

	protected abstract void doRun();

	@Override
	public void setMetadataTypeManager(IMetadataTypeManager<K> typeManager) {
		metadataTypeManager = typeManager;
	}

	@Override
	public void setRepository(IRepository<IELO<K>, K> repo) {
		repository = repo;
	}

	// public IMetadataTypeManager<K> getMetadataTypeManager() {
	// return toolBrokerAPI.getMetaDataTypeManager();
	// }

}
