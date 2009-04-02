package eu.scy.agents.impl;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.agents.api.IProcessingAgent;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

public abstract class AbstractProcessingAgent<K extends IMetadataKey> extends
		AbstractAgent implements IProcessingAgent<K> {

	protected boolean done;
	private ToolBrokerAPI<K> toolBrokerAPI;

	@Override
	public void run() {
		while (!done) {
			doRun();
		}
	}

	public AbstractProcessingAgent(String threadName) {
		this(threadName, new ToolBrokerImpl<K>());
	}

	protected AbstractProcessingAgent(String threadName,
			ToolBrokerAPI<K> toolBroker) {
		toolBrokerAPI = toolBroker;
		done = false;
		Thread t = new Thread(this, threadName);
		t.start();
	}

	protected abstract void doRun();

	public IRepository<IELO<K>, K> getRepository() {
		return toolBrokerAPI.getRepository();
	}

	public IMetadataTypeManager<K> getMetadataTypeManager() {
		return toolBrokerAPI.getMetaDataTypeManager();
	}

}
