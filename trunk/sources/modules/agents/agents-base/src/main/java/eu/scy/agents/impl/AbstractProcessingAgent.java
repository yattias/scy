package eu.scy.agents.impl;

import roolo.elo.api.IMetadataKey;
import eu.scy.agents.api.IProcessingAgent;

public abstract class AbstractProcessingAgent<K extends IMetadataKey> extends
		AbstractThreadedAgent implements IProcessingAgent<K> {

	public AbstractProcessingAgent(String threadName) {
		super(threadName);
	}

	// @Override
	// public ToolBrokerAPI<K> getToolBrokerAPI() {
	// return new ToolBrokerImpl<K>();
	// }

}
